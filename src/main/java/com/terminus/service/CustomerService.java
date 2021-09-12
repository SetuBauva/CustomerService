package com.terminus.service;

import java.util.List;

import com.terminus.entity.CustomerEntity;

public interface CustomerService {

	public List<CustomerEntity> getAllCustomerDetails();

	public List<CustomerEntity> getcustomerDetails(int customerId);

	public int updateStatus(String status, int customerId, int phoneNumber);
}
