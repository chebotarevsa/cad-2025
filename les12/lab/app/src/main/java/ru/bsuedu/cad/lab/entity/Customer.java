package ru.bsuedu.cad.lab.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@Table(name = "CUSTOMERS")
public class Customer {
	@Id
	@Column(name = "customer_id")
	private int customerId;
	private String name;
	private String email;
	private String phone;
	private String address;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<Order> orders = new ArrayList<>();

	public Customer(int customerId, String name, String email, String phone, String address) {
		this.customerId = customerId;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

	public Customer() {
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
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

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}
