package com.sprint.user_service.exceptions;

public class InvalidCredentialsException extends RuntimeException {
 
	public InvalidCredentialsException(String messege) {
		super(messege);
	}
}
