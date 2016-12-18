package com.example.customers.dao;

import com.example.customers.domain.Customer;

import javax.inject.Named;

@Named
public class FakeLegacyRepository implements LegacyRepository {

    @Override
    public boolean save(Customer customer) {
        return true;
    }
}
