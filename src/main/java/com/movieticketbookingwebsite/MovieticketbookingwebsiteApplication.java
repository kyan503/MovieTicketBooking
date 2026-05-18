package com.movieticketbookingwebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Kích hoạt tính năng chạy định kỳ
public class MovieticketbookingwebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieticketbookingwebsiteApplication.class, args);
	}

}
