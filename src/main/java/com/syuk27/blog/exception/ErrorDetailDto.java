package com.syuk27.blog.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDetailDto {

	private LocalDateTime timestamp;
	private String message;
	private String details;

}
