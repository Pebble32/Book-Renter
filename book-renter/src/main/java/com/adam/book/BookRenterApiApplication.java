package com.adam.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BookRenterApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookRenterApiApplication.class, args);
	}

}
