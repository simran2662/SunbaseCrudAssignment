package com.sunbase.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequestModel {
	
	private String email; // User's email address for authentication
	
	private String password; // User's password for authentication
}
