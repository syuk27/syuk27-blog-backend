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

@Service
public class JwtTokenService {
    
    private final JwtEncoder jwtEncoder;

    public JwtTokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication authentication) {

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
                        .expiresAt(Instant.now().plus(90, ChronoUnit.MINUTES)) //만료 시간
                        .subject(authentication.getName()) //사용자 정보
                        .claim("scope", scope) // 사용자 역할 정보 포함
                        .build();

        // JWT 토근 생성
        return this.jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}
