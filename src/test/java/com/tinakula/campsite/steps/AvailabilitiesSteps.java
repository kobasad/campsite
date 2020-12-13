package com.tinakula.campsite.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinakula.campsite.domain.model.Availability;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class AvailabilitiesSteps implements En {


	public AvailabilitiesSteps(MockMvc mvc, ObjectMapper objectMapper, World world) {

		When("I list availabilities with these criteria:", (DataTable queryParamsTable) -> {

			Map<String, String>  queryParamsMap = queryParamsTable.asMap(String.class, String.class);

			MockHttpServletRequestBuilder get = get("/v1/availabilities/")
					.accept(MediaType.APPLICATION_JSON_VALUE)
					.queryParam("start", queryParamsMap.get("start"));

			String end = queryParamsMap.get("end");

			if (!end.isBlank()) {
				get.queryParam("end", end);
			}

			world.setLatestRequestResult(
					mvc.perform(get));
		});

		Then("the following availabilities are listed:", (DataTable availabilitiesTable) -> {
			List<Availability> expectedAvailabilities = availabilitiesTable.asMaps().stream()
					.map(AvailabilityUtil::buildAvailability)
					.collect(toList());

			List<Availability> actualAvailabilities =
					objectMapper.readValue(
							world.getLatestRequestResult().andReturn()
									.getResponse()
									.getContentAsString(),
							new TypeReference<>() {	});

			assertThat(actualAvailabilities).hasSize(expectedAvailabilities.size());

			IntStream.range(0, expectedAvailabilities.size())
					.forEach(i -> assertAvailability(actualAvailabilities.get(i), expectedAvailabilities.get(i)));
		});
	}

	private void assertAvailability(Availability actual, Availability expected) {
		assertThat(actual.getStartDate().toInstant()).isEqualTo(expected.getStartDate().toInstant());
		assertThat(actual.getEndDate().toInstant()).isEqualTo(expected.getEndDate().toInstant());
	}
}
