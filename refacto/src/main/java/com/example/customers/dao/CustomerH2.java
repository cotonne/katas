package com.example.customers.dao;

import javax.persistence.*;
import java.util.Date;

@Entity
public class CustomerH2 {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    public String firstName;
    public String lastName;
    public String ref;
    public Date creation;
    @Version
    public Long version;
    public String address;
    @OneToOne
    public Point position;
    public String identifier;

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

    public boolean hasPosition() {
        return position == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerH2 that = (CustomerH2) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        return lastName != null ? lastName.equals(that.lastName) : that.lastName == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }
}
