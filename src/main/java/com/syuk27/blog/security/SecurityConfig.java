package com.syuk27.blog.security;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity	// 3.x 이상 생략 가능하지만 가독성을 위해 추가하는 것이 좋음
@EnableMethodSecurity  // 메서드 보안을 활성화해야 @PreAuthorize, @Secured 사용 가능
public class SecurityConfig {
	
	private final String authUrl;

	SecurityConfig(@Value("${app.auth.url}") String authUrl) {
		this.authUrl = authUrl;
	}
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector) throws Exception {
        
        // https://github.com/spring-projects/spring-security/issues/12310
        return httpSecurity
        		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                    //Spring Security는 설정된 순서대로 규칙을 적용
                	
                	// get만 허용
            		.requestMatchers(HttpMethod.GET,"/**").permitAll()

            		// 관리자 페이지
//                	.requestMatchers(HttpMethod.GET, "/admin/posts/**").permitAll()
                	.requestMatchers("/admin/**").hasRole("ADMIN")
                	
                	// 로그인 전 허용 페이지 모든 http
                    .requestMatchers("/authenticate/**", "/auth/register/**").permitAll()
                    
                    // 로그인 후 허용 페이지 모든 http
                    .requestMatchers("/test/**").authenticated()
                    
                    .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.
                    sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 생성하지 않음
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                		.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        		) //OAuth2 리소스 서버 설정 => JWT, JWTConverter 사용
                .httpBasic(Customizer.withDefaults()) //기본 HTTP Basic 인증 활성화
                .headers(header -> header.frameOptions().sameOrigin()) //X-Frame-Options 설정 동일 출처에서만 <iframe>을 허용.
                .build();
    }
    
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of(this.authUrl)); // 특정 주소만 허용
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드 설정
		configuration.setAllowedHeaders(List.of("*")); // 모든 요청 헤더 허용
		configuration.setAllowCredentials(true); // 쿠키와 같은 인증 정보 허용 여부

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // 특정 URI 제외하고 모든 API에 CORS 적용 => 기본적으로 모든 URI 적용
//        source.registerCorsConfiguration("/jpa/find_users", configuration); // 특정 URI만 CORS 적용
		return source;
	}
    
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
    	
    	JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
    	converter.setAuthorityPrefix("ROLE_");
    	converter.setAuthoritiesClaimName("scope");

    	JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
    	authenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
    	return authenticationConverter;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService) {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        JWKSet jwkSet = new JWKSet(rsaKey());
        return (((jwkSelector, securityContext) 
                        -> jwkSelector.select(jwkSet)));
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    JwtDecoder jwtDecoder() throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(rsaKey().toRSAPublicKey())
                .build();
    }
    
    @Bean
    public RSAKey rsaKey() {
        
        KeyPair keyPair = keyPair();
        
        return new RSAKey
                .Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public KeyPair keyPair() {
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to generate an RSA Key Pair", e);
        }
    }
    
}
