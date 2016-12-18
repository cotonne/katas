package com.example.customers.utils;

import com.example.customers.dao.CustomerH2;
import com.example.customers.domain.Customer;

public class CustomerH2Mapper {
    public static CustomerH2 format(Customer customer) {
        CustomerH2 c = new CustomerH2();
        c.id = customer.id;
        c.identifier = customer.identifier;
        c.address = customer.address;
        c.creation = customer.dateCreated;
        return c;
    }
}
