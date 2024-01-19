package com.qosocial.v1api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class V1ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(V1ApiApplication.class, args);
	}

}
