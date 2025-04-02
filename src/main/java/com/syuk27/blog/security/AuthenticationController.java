package com.syuk27.blog.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

		//UsernamePasswordAuthenticationToken 클래스는 Authentication 인터페이스 구현 객체 => Authentication 타입으로 받을 수 있음
		var authentication = authenticationManager.authenticate(authenticationToken); 
		//authenticationManager => 인증 진행 CustomUserDetailsService에서 리턴한 비번과 비교 PasswordEncoder.matches(rawPassword, encodedPassword)

		//jwt token 및 쿠키 저장
		String token = jwtTokenService.generateToken(response, authentication);
		jwtTokenService.addJwtToCookie(response, token);
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
		jwtTokenService.removeJwtToCookie(response);
		
		return ResponseEntity.ok().build();
	}
}