package com.tinakula.campsite;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "classpath:features",
		glue = "com.tinakula.campsite.steps",
        plugin = {"summary", "pretty", "html:target/cucumber-report", "json:target/cucumber.json"},
		tags = { "not @ignore" })
@Slf4j
public class RunFeatureTests {

	@ClassRule
	public static PostgresContainer postgresContainer = PostgresContainer.getInstance();
}
