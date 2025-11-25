package com.example.WebNovelReviewSite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WebNovelReviewSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebNovelReviewSiteApplication.class, args);
	}

}
