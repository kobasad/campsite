package com.tinakula.campsite.steps;

import io.cucumber.java8.En;
import lombok.Data;
import org.springframework.test.web.servlet.ResultActions;

@Data
public class World implements En {

	private ResultActions latestRequestResult;


}
