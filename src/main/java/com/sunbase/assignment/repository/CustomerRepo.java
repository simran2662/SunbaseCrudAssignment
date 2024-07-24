package com.sunbase.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sunbase.assignment.entity.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long>{

}
