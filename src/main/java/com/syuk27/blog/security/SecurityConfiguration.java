package com.syuk27.blog.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
//			.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
		// authorizeHttpRequests() → HTTP 요청에 대한 보안 규칙을 정의하는 메서드.
//				anyRequest() → 모든 요청을 의미함 (/**와 동일).
//				authenticated() → 인증된 사용자만 접근 가능하도록 설정.
//				이 설정을 적용하면, 모든 API 요청은 인증되지 않으면 401 Unauthorized 에러가 발생.
//				즉, 로그인하지 않은 사용자는 API를 사용할 수 없음
		
//				.authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").denyAll() // 특정 URI는 차단
//						.anyRequest().permitAll() // 나머지 모든 API 허용
//				)

				.authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").denyAll() // 특정 URI는 차단
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.anyRequest().authenticated() // OPTIONS 요청 허용, 나머지 차단 
				)
				.httpBasic(Customizer.withDefaults()) // HTTP 기본 인증 활성화

				.csrf(csrf -> csrf.disable())

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				//서버에서 세션을 생성하지 않음
//				클라이언트가 모든 요청에서 인증 정보를 다시 보내야 함 (예: JWT)
//				CSRF 보호 기능도 보통 비활성화함 (csrf().disable())

				.cors(cors -> cors.configurationSource(corsConfigurationSource())); // CORS 설정 적용

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// 특정 React 주소만 허용
		configuration.setAllowedOrigins(List.of("http://localhost:5173"));

		// 허용할 HTTP 메서드 설정
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));

		// 모든 요청 헤더 허용
		configuration.setAllowedHeaders(List.of("*"));

		// 인증 정보 포함 여부 (필요 시 설정)
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		// 특정 URI 제외하고 모든 API에 CORS 적용
		source.registerCorsConfiguration("/**", configuration); // 기본적으로 모든 URI 적용
		
		// 특정 URI만 CORS 적용
//        source.registerCorsConfiguration("/jpa/find_users", configuration); 

		return source;
	}
	
	@Bean
	public UserDetailsService userDetailsService(
			@Value("${security-username}") String userName,
			@Value("${security-password}") String password
			) {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username(userName)
            .password(password)
            .roles("USER")
            .build();
        
        return new InMemoryUserDetailsManager(user);
    }
}
