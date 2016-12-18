package com.example.customers.controllers;

import com.example.customers.dao.CustomerH2;
import com.example.customers.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerH2Dao extends JpaRepository<CustomerH2, Long> {
    Optional<CustomerH2> findOneByRef(String identifier);

    default CustomerH2 saveAndFlush(CustomerH2 customer){
        /// TODO : persist to DB and return the saved customer
        return new CustomerH2();
    }
}
