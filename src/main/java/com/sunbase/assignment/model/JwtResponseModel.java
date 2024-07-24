package com.sunbase.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseModel {
	
	private String username; // Username associated with the JWT token
	
	private String token; // JWT token issued for authentication
	
	private boolean success;
}
