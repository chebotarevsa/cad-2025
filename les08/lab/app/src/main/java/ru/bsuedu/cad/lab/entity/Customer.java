package ru.bsuedu.cad.lab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;


@Entity
@Table(name = "Customers")
public class Customer {
    @Id
    @Column(name="customer_id")
    private Long customerID;
    private String name;
    private String email;
    private String phone;
    private String address;

    public Customer(){
        
    }
    
    public Customer(Long customerID, String name, String email, String phone, String address) {
        this.customerID = customerID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
