package com.example.customers.dao;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Point {

    @Id
    public Long id;
    public double x;
    public double y;
}
