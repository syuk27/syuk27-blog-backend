package com.syuk27.blog.exceptin;

import java.text.MessageFormat;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorType {
	
	/*
     * 400 BAD_REQUEST: 잘못된 요청
     * 401 UNAUTHORIZED: 인증되지 않은 사용자의 요청
     * 403 FORBIDDEN: 권한이 없는 사용자의 요청
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     * 409 CONFLICT
     * 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */

	POST_EX01(HttpStatus.NOT_FOUND, "존재하지 않는 게시글 입니다."),
	
	USER_EX01(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."),
	USER_EX02(HttpStatus.CONFLICT, "이미 사용중인 닉네임 입니다. 닉네임: {0}"),
	USER_EX03(HttpStatus.CONFLICT, "이미 사용중인 이메일 입니다. 이메일: {0}"),
	USER_EX04(HttpStatus.CONFLICT, "이미 사용중인 비밀번호 입니다.");

	private final HttpStatus httpStatus;
	
	private final String message;

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getMessage(Object... values) { // Object... => 내부적으로 Object[] 배열로 변환됨, 여러개 값을 가변적으로 받음
		return MessageFormat.format(message, values);
	}
}
