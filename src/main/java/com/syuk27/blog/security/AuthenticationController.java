package com.syuk27.blog.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

	private final JwtTokenService jwtTokenService;

	private final AuthenticationManager authenticationManager;

	public AuthenticationController(JwtTokenService jwtTokenService, AuthenticationManager authenticationManager) {
		this.jwtTokenService = jwtTokenService;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody JwtTokenRequest jwtTokenRequest, HttpServletResponse response) {

		var authenticationToken = new UsernamePasswordAuthenticationToken(
				jwtTokenRequest.email(),
				jwtTokenRequest.password());

		var authentication = authenticationManager.authenticate(authenticationToken);

		SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보 저장
		jwtTokenService.generateToken(response, authentication);

		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
		jwtTokenService.removeToken(response);
		
		return ResponseEntity.ok().build();
	}
}