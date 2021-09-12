package com.terminus.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity Class representing Customer details table
 */
@Entity
@Table(name = "customer_details")
public class CustomerEntity {

	@Id
	@GeneratedValue
	@JsonIgnore
	private int id;
	private int customerID;
	private int phoneNumber;
	private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "PhoneNumberEntity [id=" + id + ", customerID=" + customerID + ", phoneNumber=" + phoneNumber
				+ ", status=" + status + "]";
	}

	public CustomerEntity(int id, int customerID, int phoneNumber, String status) {
		super();
		this.id = id;
		this.customerID = customerID;
		this.phoneNumber = phoneNumber;
		this.status = status;
	}

	public CustomerEntity() {
		super();
	}

}
