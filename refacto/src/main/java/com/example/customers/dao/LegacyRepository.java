package com.example.customers.dao;

import com.example.customers.domain.Customer;

/**
 * Created by user on 18/12/2016.
 */
public interface LegacyRepository {
    boolean save(Customer customer);
}
