package com.syuk27.blog.security;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
	
	public JwtAuthenticationFilter(JwtDecoder jwtDecoder) {
		this.jwtDecoder = jwtDecoder;
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
				SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));
			} catch (Exception e) {
				SecurityContextHolder.clearContext();
			}
		}
		
		filterChain.doFilter(request, response);
	}
}
