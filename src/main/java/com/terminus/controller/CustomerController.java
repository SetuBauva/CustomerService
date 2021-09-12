package com.terminus.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.terminus.entity.CustomerEntity;
import com.terminus.message.MessageSender;
import com.terminus.service.CustomerService;

/**
 * Controller for the Rest API's
 */
@RestController
@JsonInclude(Include.NON_EMPTY)
public class CustomerController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CustomerService customerService;

	@Autowired
	MessageSender messageSender;

	@GetMapping("/customerDetails")
	@Cacheable(value = "customerDetails", key = "#root.method")
	public List<CustomerEntity> getAllCustomerDetails() {
		logger.info("Getting All Customer Data");
		return customerService.getAllCustomerDetails();
	}

	@GetMapping("/customerDetails/{customerId}")
	public List<CustomerEntity> getcustomerDetails(@PathVariable("customerId") int customerId) {
		logger.info("Getting Customer Data");
		return customerService.getcustomerDetails(customerId);
	}

	@PatchMapping("/customerStatus")
	public ResponseEntity<CustomerEntity> updatePhoneNumber(@RequestBody CustomerEntity customerEntity) {
		if (customerService.updateStatus(customerEntity.getStatus(), customerEntity.getCustomerID(), customerEntity.getPhoneNumber()) > 0) {
			try {
				logger.info("Customer Data Updated");
				messageSender.sendCustomerInfo(customerEntity.getCustomerID(), customerEntity.getPhoneNumber(), customerEntity.getStatus());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new ResponseEntity<CustomerEntity>(customerEntity, HttpStatus.OK);
		} else {
			logger.error("Invalid Data to Update");
			return new ResponseEntity<CustomerEntity>(HttpStatus.NOT_FOUND);
		}
	}

	public void receiveMessage(String customerId, String phoneNumber, String status) {
		logger.info("Message Received");
		// Process the received message as required
	}

}
