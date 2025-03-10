package com.syuk27.blog.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtTokenService {
    
    private final JwtEncoder jwtEncoder;

    public JwtTokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public void generateToken(HttpServletResponse response, Authentication authentication) {

    	int expires = 90;
    	
    	// 사용자의 권한을 Scope로 변환
        var scope = authentication
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(" "));

        // JWT Claims 설정
        var claims = JwtClaimsSet.builder()
                        .issuer("self") //발급자
                        .issuedAt(Instant.now()) //발급 시간
                        .expiresAt(Instant.now().plus(Long.valueOf(expires), ChronoUnit.MINUTES)) // 90분 후 만료
                        .subject(authentication.getName()) //사용자 정보
                        .claim("scope", scope) // 사용자 역할 정보 포함
                        .build();

        // JWT 토근 생성
		String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		
		Cookie cookie = new Cookie("jwt", token);
		cookie.setHttpOnly(true); // javascript 접근 불가
		cookie.setSecure(true); // https에서만 전송
		cookie.setPath("/"); // 전체 도메인에서 사용 가능
		cookie.setMaxAge(expires * 60); // 90분
		
		response.addCookie(cookie);
    }
    
    public void removeToken(HttpServletResponse response) {
    	Cookie cookie = new Cookie("jwt", null);
    	cookie.setHttpOnly(true);
    	cookie.setSecure(true);
    	cookie.setPath("/");
    	cookie.setMaxAge(0);
    	
    	response.addCookie(cookie);
    }
}
