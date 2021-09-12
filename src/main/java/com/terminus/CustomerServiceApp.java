package com.terminus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.terminus" })
@EnableCaching
public class CustomerServiceApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CustomerServiceApp.class).properties("spring.config.name: CustomerService");
	}

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "CustomerService");
		SpringApplication.run(CustomerServiceApp.class, args);
	}
}
