package com.sprint.user_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sprint.user_service.utility.ErrorResponse;



@RestControllerAdvice
public class AuthenticationExceptionHandler {

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<Object> handleInvalidCredentials(InvalidCredentialsException ex){
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage()));
	}
	
	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<Object> handleUnauthorizedAccess(UnauthorizedAccessException ex){
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}
}
