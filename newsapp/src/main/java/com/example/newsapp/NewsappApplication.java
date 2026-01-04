package com.example.newsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NewsappApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsappApplication.class, args);
	}

}
