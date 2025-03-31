package com.sprint.user_service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sprint.user_service.dto.AuthResponseDto;
import com.sprint.user_service.dto.LoginDto;
import com.sprint.user_service.model.User;
import com.sprint.user_service.service.AuthService;
import com.sprint.user_service.utility.JwtService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthService authenticationService;

	public AuthenticationController(JwtService jwtService, AuthService authenticationService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}

	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody User registerUserDto) {
		User registeredUser = authenticationService.signup(registerUserDto);

		return ResponseEntity.ok(registeredUser);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDto> authenticate(@RequestBody LoginDto loginUserDto) {
		User authenticatedUser = authenticationService.authenticate(loginUserDto);

		String jwtToken = jwtService.generateToken(authenticatedUser);

		AuthResponseDto loginResponse = new AuthResponseDto();
		loginResponse.setToken(jwtToken);
		loginResponse.setUserName(loginUserDto.getEmail());
		loginResponse.setExpiresIn(jwtService.getExpirationTime());

		return ResponseEntity.ok(loginResponse);
	}

	@GetMapping("validate")
	public ResponseEntity<?> validate(@RequestHeader("Authorization") String token) throws JsonProcessingException {
		log.info("Received request to validate token: {}", token);
		Map<String, String> res = new HashMap<String, String>();
		if (authenticationService.validateToken(token)) {
			log.info("Token is valid: {}", token);
			
			res.put("message", "valid");
			// producer.publishAuthDatum(tokenService.getUsername(token), "TOKEN
			// VALIDATED");
			return ResponseEntity.ok(res);
		} else {
			log.info("Token is invalid: {}", token);
			res.put("message", "Invalid");
			return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
		}
	}

//    @GetMapping("logout")
//    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) throws JsonProcessingException {
//
//    	authenticationService.invalidateToken(token);
//        //producer.publishAuthDatum(tokenService.getUsername(token), "LOGOUT");
//        return ResponseEntity.ok("Logged out successfully");
//    }

}
