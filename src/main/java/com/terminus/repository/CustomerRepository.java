package com.terminus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.terminus.entity.CustomerEntity;

/**
 * Repository for performing Database operations
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

	public List<CustomerEntity> findByCustomerID(int customerID);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "Update CUSTOMER_DETAILS set status = ?1 where customerId = ?2\n"
			+ "and phone_number = ?3", nativeQuery = true)
	public int updateStatus(String status, int customerId, int phone_number);

}
