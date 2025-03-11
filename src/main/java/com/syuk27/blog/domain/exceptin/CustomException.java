package com.syuk27.blog.domain.exceptin;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final HttpStatus status;
	
	public CustomException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}
}
