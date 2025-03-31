package com.sprint.user_service.exceptions;

public class UnauthorizedAccessException extends RuntimeException {


	public UnauthorizedAccessException(String message) {
		super(message);
	}
}
