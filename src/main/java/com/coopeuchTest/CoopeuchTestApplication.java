package com.coopeuchTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"com.coopeuchTest"})
@SpringBootApplication
@EnableJpaRepositories("com.coopeuchTest.dao")
@EntityScan("com.coopeuchTest.model")
@Configuration
@EnableAutoConfiguration
public class CoopeuchTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoopeuchTestApplication.class, args);
	}

}
