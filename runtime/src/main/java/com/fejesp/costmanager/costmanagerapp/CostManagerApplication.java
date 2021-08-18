package com.fejesp.costmanager.costmanagerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCostManagerService
@SpringBootApplication
public class CostManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CostManagerApplication.class, args);
	}

}
