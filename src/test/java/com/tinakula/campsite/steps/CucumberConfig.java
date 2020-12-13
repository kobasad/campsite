package com.tinakula.campsite.steps;

import com.tinakula.campsite.domain.service.TimeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.mock;

@Profile("test")
@ContextConfiguration
public class CucumberConfig {
	@Bean
	TimeService timeService() {
		return mock(TimeService.class);
	}

    @Bean
	MockMvc mvc(WebApplicationContext context) {
		return MockMvcBuilders
				.webAppContextSetup(context)
				.build();
	}
}
