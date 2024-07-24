package com.sunbase.assignment.controller;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunbase.assignment.model.JwtRequestModel;
import com.sunbase.assignment.model.JwtResponseModel;
import com.sunbase.assignment.security.JwtAuthToken;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtAuthToken jwtAuthToken;
	
	// Endpoint for user login with JWT authentication
	@PostMapping("/login")
	public ResponseEntity<JwtResponseModel> login(@RequestBody JwtRequestModel request) {
		// Authenticate user credentials
		doAuthenticate(request.getEmail(), request.getPassword());
		
		// Load user details
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		
		// Generate JWT token
		String token = jwtAuthToken.generateToken(userDetails);
		
		// Create and return JWT response model with token and username
		JwtResponseModel response = JwtResponseModel.builder()
				.token(token)
				.username(userDetails.getUsername())
				.build();
		response.setSuccess(true);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Method to authenticate user credentials using AuthenticationManager
	private void doAuthenticate(String email, String password) {
		// Create authentication token with provided email and password
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
		
		try {
			// Perform authentication
			authenticationManager.authenticate(authentication);
		} catch (BadCredentialsException e) {
			// Handle authentication failure (invalid credentials)
			throw new BadCredentialsException("Invalid Username or Password");
		}
	}

	// Exception handler for BadCredentialsException
	@ExceptionHandler(BadCredentialsException.class)
	public String exceptionHandler() {
		// Return custom message for authentication failure
		return "Invalid Username or Password";
	}
}
