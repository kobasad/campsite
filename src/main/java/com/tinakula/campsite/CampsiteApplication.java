package com.tinakula.campsite;

import com.tinakula.campsite.domain.service.TimeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import static java.time.ZonedDateTime.now;

@SpringBootApplication
public class CampsiteApplication {

	@Bean
	@Profile("dev")
	TimeService timeService() {
		return () -> now();
	}

	public static void main(String[] args) {
		SpringApplication.run(CampsiteApplication.class, args);
	}

}
