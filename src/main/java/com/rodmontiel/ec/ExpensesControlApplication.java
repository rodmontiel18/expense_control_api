package com.rodmontiel.ec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.rodmontiel.ec.*")
@EnableAutoConfiguration
public class ExpensesControlApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ExpensesControlApplication.class, args);
	}

}
