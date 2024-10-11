package com.globalLogicTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"com.globalLogicTest"})
@SpringBootApplication
@EnableJpaRepositories("com.globalLogicTest.dao")
@EntityScan("com.globalLogicTest.model")
@Configuration
@EnableAutoConfiguration
public class GlobalLogicTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(GlobalLogicTestApplication.class, args);
	}

}
