package ru.bsuedu.cad.lab.dto;

import java.util.List;

public class CreateOrderDto {
	private int customerId;
	private List<CreateOrderDetailDto> details;
	private String address;

	public CreateOrderDto() {
	}

	public CreateOrderDto(int customerId, List<CreateOrderDetailDto> details, String address) {
		this.customerId = customerId;
		this.details = details;
		this.address = address;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public List<CreateOrderDetailDto> getDetails() {
		return details;
	}

	public void setDetails(List<CreateOrderDetailDto> details) {
		this.details = details;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
