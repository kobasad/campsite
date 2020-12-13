package com.tinakula.campsite.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinakula.campsite.domain.service.TimeService;
import com.tinakula.campsite.web.v1.dto.ErrorResponse;
import io.cucumber.java8.En;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommonSteps implements En {

	private World world;

	public CommonSteps(TimeService timeService, ObjectMapper objectMapper, World world) {

		this.world = world;

		Given("it is {string} now", (String currentTime) -> {
			given(timeService.currentTime())
					.willReturn(ZonedDateTime.parse(currentTime));
		});

		Then("the operation is successful", () -> {
			assertResponseStatus(status().isOk());
		});

		Then("the operation fails as a conflict", () -> {

			assertResponseStatus(status().isConflict());

		});

		Then("the operation fails as bad request", () -> {

			assertResponseStatus(status().isBadRequest());

		});

		Then("the operation fails as not found", () -> {

			assertResponseStatus(status().isNotFound());

		});

		Then("there is an error: {string}", (String expectedError) -> {
			ErrorResponse response = objectMapper.readValue(
					world.getLatestRequestResult().andReturn().getResponse().getContentAsString(), ErrorResponse.class);

			assertThat(response.getErrors()).isNotEmpty();
			assertThat(response.getErrors()).contains(expectedError);
		});
	}

	private void assertResponseStatus(ResultMatcher statusMatcher) throws Exception {
		try {
			world.getLatestRequestResult().andExpect(statusMatcher);
		} catch (AssertionError e) {
			world.getLatestRequestResult().andDo(print());
			throw e;
		}
	}
}
