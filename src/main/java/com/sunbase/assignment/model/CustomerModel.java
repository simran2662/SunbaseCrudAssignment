package com.sunbase.assignment.model;

import lombok.Data;

@Data
public class CustomerModel {
	private Long id;

	private String firstName;

	private String lastName;

	private String street;

	private String address;

	private String city;

	private String state;

	private String email;

	private String phone;
}
