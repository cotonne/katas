package com.example.customers.domain;

import com.example.customers.dao.Point;

import java.util.Date;

/**
 * Created by user on 10/12/2016.
 */
public class Customer {
    public String identifier;
    public Long version;
    public Date dateCreated;
    public Long id;
    public String address;
    public Point position;
    public Title title;

    @Override
    public String toString() {
        return "Customer{" +
                "identifier='" + identifier + '\'' +
                ", version=" + version +
                ", dateCreated=" + dateCreated +
                ", id=" + id +
                ", address='" + address + '\'' +
                ", position=" + position +
                ", title=" + title +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (identifier != null ? !identifier.equals(customer.identifier) : customer.identifier != null) return false;
        if (id != null ? !id.equals(customer.id) : customer.id != null) return false;
        if (address != null ? !address.equals(customer.address) : customer.address != null) return false;
        if (position != null ? !position.equals(customer.position) : customer.position != null) return false;
        return title == customer.title;
    }

    @Override
    public int hashCode() {
        int result = identifier != null ? identifier.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}
