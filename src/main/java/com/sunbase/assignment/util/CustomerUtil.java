package com.sunbase.assignment.util;

import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.sunbase.assignment.entity.Customer;

/**
 * Utility class for updating customer details.
 */
@Service
public class CustomerUtil {

    /**
     * Updates the fields of the given customer object based on non-null and non-empty values from another customer object.
     *
     * @param customer  The customer object to update.
     * @param customer2 The customer object containing updated values.
     */
    public void updateCustomerDetails(Customer customer, Customer customer2) {
        if (!Strings.isNullOrEmpty(customer2.getFirstName()))
            customer.setFirstName(customer2.getFirstName());
        if (!Strings.isNullOrEmpty(customer2.getLastName()))
            customer.setLastName(customer2.getLastName());
        if (!Strings.isNullOrEmpty(customer2.getStreet()))
            customer.setStreet(customer2.getStreet());
        if (!Strings.isNullOrEmpty(customer2.getAddress()))
            customer.setAddress(customer2.getAddress());
        if (!Strings.isNullOrEmpty(customer2.getCity()))
            customer.setCity(customer2.getCity());
        if (!Strings.isNullOrEmpty(customer2.getState()))
            customer.setState(customer2.getState());
        if (!Strings.isNullOrEmpty(customer2.getEmail()))
            customer.setEmail(customer2.getEmail());
        if (!Strings.isNullOrEmpty(customer2.getPhone()))
            customer.setPhone(customer2.getPhone());
    }
}
