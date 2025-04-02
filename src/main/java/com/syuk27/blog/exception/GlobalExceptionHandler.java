package com.syuk27.blog.exception;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ErrorDetailDto> handleAllException(Exception ex, WebRequest request) throws Exception {
		ErrorDetailDto errorDetailDto = new ErrorDetailDto(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity<ErrorDetailDto>(errorDetailDto, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(CustomException.class)
	public final ResponseEntity<ErrorDetailDto> handleCustomException(CustomException ex, WebRequest request) throws Exception {
		
		HttpStatus httpStatus = Optional.ofNullable(ex.getStatus()).orElse(HttpStatus.BAD_REQUEST);
		ErrorDetailDto errorDetailDto = new ErrorDetailDto(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity<ErrorDetailDto>(errorDetailDto, httpStatus);
	}
}
