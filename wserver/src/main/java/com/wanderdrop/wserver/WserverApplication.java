package com.wanderdrop.wserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com/wanderdrop/wserver/model")
public class WserverApplication {

	public static void main(String[] args) {

		SpringApplication.run(WserverApplication.class, args);
	}
}
