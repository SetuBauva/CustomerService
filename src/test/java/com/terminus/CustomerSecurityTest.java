package com.terminus;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.terminus.config.SpringSecurityConfig;
import com.terminus.entity.CustomerEntity;
import com.terminus.repository.CustomerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {SpringSecurityConfig.class,CustomerServiceApp.class})
@AutoConfigureMockMvc
public class CustomerSecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerRepository customerRepository;

	List<CustomerEntity> customer;

	@Before
	public void init() {
		customer = new ArrayList<CustomerEntity>();
		CustomerEntity phoneNumber1 = new CustomerEntity(101, 1, 111111111, "Active");
		customer.add(phoneNumber1);
	}

	@WithMockUser("USER")
	@Test
	public void getCustomerSuccess() throws Exception {

		when(customerRepository.findByCustomerID(1)).thenReturn(customer);

		mockMvc.perform(get("/customerDetails/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].customerID", is(1))).andExpect(jsonPath("$[0].phoneNumber", is(111111111)))
				.andExpect(jsonPath("$[0].status", is("Active")));
	}

	@Test
	public void getCustomerUnauthorized() throws Exception {
		mockMvc.perform(get("/customerDetails/1")).andDo(print()).andExpect(status().isUnauthorized());
	}

	@WithMockUser("USER")
	@Test
	public void getAllCustomerSuccess() throws Exception {

		CustomerEntity phoneNumber2 = new CustomerEntity(104, 2, 444444444, "Active");
		customer.add(phoneNumber2);

		when(customerRepository.findAll()).thenReturn(customer);

		mockMvc.perform(get("/customerDetails")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[*].customerID").value(Lists.newArrayList(1, 2)))
				.andExpect(jsonPath("$[*].phoneNumber").value(Lists.newArrayList(111111111, 444444444)))
				.andExpect(jsonPath("$[*].status").value(Lists.newArrayList("Active", "Active")));
	}

	@Test
	public void getAllCustomerUnauthorized() throws Exception {
		mockMvc.perform(get("/customerDetails")).andDo(print()).andExpect(status().isUnauthorized());
	}

	@WithMockUser("USER")
	@Test
	public void updateCustomerStatusUnauthorized() throws Exception {
		mockMvc.perform(patch("/customerStatus")).andDo(print()).andExpect(status().isForbidden());
	}
}
