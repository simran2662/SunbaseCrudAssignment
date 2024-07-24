package com.sunbase.assignment.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sunbase.assignment.dto.CustomerFilterDto;
import com.sunbase.assignment.entity.Customer;
import com.sunbase.assignment.model.CustomerModel;
import com.sunbase.assignment.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	CustomerService service;
	
	// Endpoint to save or update a customer
	@PostMapping("/saveOrUpdate")
	public Customer saveOrUpdateCustomer(@RequestBody CustomerModel requestModel) {
		return service.saveOrUpdateCustomer(requestModel);
	}
	
	// Endpoint to retrieve customers based on filter criteria
	@GetMapping("/getCustomer")
	public List<Customer> getCustomer(@Valid CustomerFilterDto filterDto){
		return service.getCustomer(filterDto);
	}
	
	// Endpoint to delete a customer by ID
	@DeleteMapping("/{id}")
	public String deleteCustomer(@PathVariable("id") Long id) {
		return service.deleteCustomer(id);
	}
}
