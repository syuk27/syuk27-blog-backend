package com.syuk27.blog.security;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Lazy
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtDecoder jwtDecoder;
	private final JwtTokenService jwtTokenService;
	
	public JwtAuthenticationFilter(JwtDecoder jwtDecoder, JwtTokenService jwtTokenService) {
		this.jwtDecoder = jwtDecoder;
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if("jwt".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
		}
		
		if(token != null) {
			try {
				Jwt jwt = jwtDecoder.decode(token);
				String username = jwt.getClaim("sub");
				String scope = jwt.getClaim("scope");
				
				List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(scope));
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						username, null, authorities); // 시큐리티에서 찾는 권한
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//				SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));
				
				Instant expiration = jwt.getExpiresAt();
				Duration remainingTime = Duration.between(Instant.now(), expiration);
				
				if(remainingTime.toMinutes() < 30) {
					// jwt token 새로 발행 및 쿠키 저장
					String newToken = jwtTokenService.generateToken(response, authenticationToken);
					jwtTokenService.addJwtToCookie(response, newToken);
				}
			} 
			catch (Exception e) {
				SecurityContextHolder.clearContext();
			}
		}
		
		filterChain.doFilter(request, response);
	}
}
