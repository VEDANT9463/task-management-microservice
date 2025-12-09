package com.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@EnableJpaRepositories(basePackages = "com.task.repository")
@EntityScan(basePackages = "com.task.entity")
@SpringBootApplication
public class TaskApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}
}
