package com.sunbase.assignment.dto;

import lombok.Data;

@Data
public class CustomerFilterDto {
	private Long id;

	private String firstName;

	private String lastName;

	private String street;

	private String address;

	private String city;

	private String state;

	private String email;

	private String phone;
	
	private String commonSearch;
	

	// Pagination parameters
	private Integer page; // Page number
	
	private Integer size; // Number of items per page
	
	// Sorting parameters
	private String sort; // Field to sort by
	
	private String sortOrder; // Sort order (ASC or DESC)
}
