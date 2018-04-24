package com.diploma.law;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class LawApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		//SpringApplication.run(LawApplication.class, args);
		SpringApplicationBuilder builder = new SpringApplicationBuilder(LawApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(LawApplication.class);
	}
}
