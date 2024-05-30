package com.example.Apollon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ApollonApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApollonApplication.class, args);
	}

}
