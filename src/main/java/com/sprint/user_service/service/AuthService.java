package com.sprint.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sprint.user_service.dto.LoginDto;
import com.sprint.user_service.exceptions.InvalidCredentialsException;
import com.sprint.user_service.model.User;
import com.sprint.user_service.repository.UserRepository;
import com.sprint.user_service.utility.JwtService;

import io.jsonwebtoken.Claims;

@Service
public class AuthService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationManager authenticationManager;

	private final JwtService jwtService;

	public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public User signup(User input) {

		if (userRepository.findByEmail(input.getEmail()).orElse(null) != null) {
			throw new InvalidCredentialsException("User name  already used");
		} else {
			User user = new User();
			user.setEmail(input.getEmail());
			user.setPassword(passwordEncoder.encode(input.getPassword()));
			user.setRole(input.getRole());
			return userRepository.save(user);
		}
	}

	public User authenticate(LoginDto input) {

		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException("Invalid username or password");
		}
		return userRepository.findByEmail(input.getEmail()).orElseThrow();
	}

	public boolean validateToken(String token) {
		String[] tokenArray = token.split(" ");
        String tokenS = tokenArray[1];
		try {
			jwtService.validateToken(tokenS);
			if (jwtService.isTokenExpired(tokenS)) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			return false;
		}

	}

}
