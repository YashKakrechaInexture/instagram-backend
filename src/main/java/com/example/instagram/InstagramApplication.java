package com.example.instagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.instagram.repository.jpa")
@EnableMongoRepositories(basePackages = "com.example.instagram.repository.mongo")
@EnableCaching
@EnableAsync
public class InstagramApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstagramApplication.class, args);
	}

}
