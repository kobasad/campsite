package com.tinakula.campsite.steps;

import io.cucumber.java8.En;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@ActiveProfiles({"test"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ComponentScan(basePackages = "com.tinakula.campsite")
@ContextConfiguration(classes = CucumberConfig.class)
@WebAppConfiguration
public class SpringContext implements En {

	public SpringContext() {
	}
}
