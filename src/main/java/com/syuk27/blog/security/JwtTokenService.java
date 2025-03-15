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

	private final long EXPIRATION_TIME = 10 * 60; // 10시간
    private final JwtEncoder jwtEncoder;

    public JwtTokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(HttpServletResponse response, Authentication authentication) {

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
                        .expiresAt(Instant.now().plus(EXPIRATION_TIME, ChronoUnit.MINUTES))
                        .subject(authentication.getName()) //사용자 정보
                        .claim("scope", scope) // 사용자 역할 정보 포함
                        .build();

        // JWT 토근 생성
		return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
    public void addJwtToCookie(HttpServletResponse response, String token) {
    	Cookie cookie = new Cookie("jwt", token);
		cookie.setHttpOnly(true); // javascript 접근 불가
		cookie.setSecure(true); // https에서만 전송
		cookie.setPath("/"); // 전체 도메인에서 사용 가능
		cookie.setMaxAge((int) EXPIRATION_TIME * 60);
		
		response.addCookie(cookie);
    }
    
    public void removeJwtToCookie(HttpServletResponse response) {
    	Cookie cookie = new Cookie("jwt", null);
    	cookie.setHttpOnly(true);
    	cookie.setSecure(true);
    	cookie.setPath("/");
    	cookie.setMaxAge(0);
    	
    	response.addCookie(cookie);
    }
}
