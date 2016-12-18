package com.example.customers.controllers;

import com.example.customers.dao.Point;
import com.example.customers.domain.Customer;

import javax.inject.Named;

@Named
public class GeocodingService {
    public Point requestGeoCoordinates(Customer customer) throws GeocodingException{
        return null;
    }
}
