package com.terminus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.terminus.entity.CustomerEntity;
import com.terminus.repository.CustomerRepository;
import com.terminus.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository repo;

	@Override
	public List<CustomerEntity> getAllCustomerDetails() {
		return repo.findAll();
	}

	@Override
	public List<CustomerEntity> getcustomerDetails(int customerId) {
		return repo.findByCustomerID(customerId);
	}

	@Override
	public int updateStatus(String status, int customerId, int phoneNumber) {
		return repo.updateStatus(status, customerId, phoneNumber);
	}

}
