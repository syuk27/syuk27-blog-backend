package com.syuk27.blog.security;

public record JwtTokenRequest(String userEmail, String password) { //record => DTO 자동 생성
	
}

