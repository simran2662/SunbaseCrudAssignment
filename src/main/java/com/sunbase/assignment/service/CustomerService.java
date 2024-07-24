package com.sunbase.assignment.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.sunbase.assignment.dto.CustomerFilterDto;
import com.sunbase.assignment.entity.Customer;
import com.sunbase.assignment.model.CustomerModel;

@Service
public interface CustomerService {

    /**
     * Saves or updates a customer based on the provided request model.
     * 
     * @param requestModel CustomerModel object containing customer details.
     * @return Saved or updated Customer entity.
     */
    Customer saveOrUpdateCustomer(CustomerModel requestModel);

    /**
     * Retrieves a list of customers based on the provided filter criteria.
     * 
     * @param filterDto CustomerFilterDto object containing filtering parameters.
     * @return List of Customer entities matching the filter criteria.
     */
    List<Customer> getCustomer(@Valid CustomerFilterDto filterDto);

    /**
     * Deletes a customer by ID.
     * 
     * @param id ID of the customer to be deleted.
     * @return Confirmation message after deleting the customer.
     */
    String deleteCustomer(Long id);
}
