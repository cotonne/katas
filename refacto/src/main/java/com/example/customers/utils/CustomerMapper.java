package com.example.customers.utils;

import com.example.customers.dao.CustomerH2;
import com.example.customers.domain.Customer;
import com.example.customers.service.NewCustomerNotification;

public class CustomerMapper {
    public static NewCustomerNotification getNewAccountNotification(Customer newCustomer) {
        // Keep only necessary parameters
        return new NewCustomerNotification();
    }

    public static NewCustomerNotification getNewAccountNotification(CustomerH2 customerH2) {
        // Keep only necessary parameters
        return new NewCustomerNotification();
    }
}
