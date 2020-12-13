package com.tinakula.campsite.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinakula.campsite.domain.model.Reservation;
import com.tinakula.campsite.domain.repository.ReservationRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.tinakula.campsite.steps.ReservationUtil.assertReservation;
import static com.tinakula.campsite.steps.ReservationUtil.buildReservation;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class ReservationSteps implements En {

	private World world;
	ReservationRepository reservationRepository;

	public ReservationSteps(MockMvc mvc, ObjectMapper objectMapper,
							ReservationRepository reservationRepository, World world) {

		this.world = world;
		this.reservationRepository = reservationRepository;

		When("I create a reservation:", (DataTable reservationTable) -> {
			Reservation reservation =
					buildReservation(reservationTable.asMap(String.class, String.class));

			world.setLatestRequestResult(
					mvc.perform(
							post("/v1/reservations")
									.content(objectMapper.writeValueAsString(reservation))
									.contentType(MediaType.APPLICATION_JSON_VALUE)));
		});

		When("I update the reservation with {string} equal to {string}:",
				(String fieldName, String fieldValue, DataTable reservationTable) -> {

			Reservation reservation =
					buildReservation(reservationTable.asMap(String.class, String.class));

			Reservation existingReservation =
					findReservation(fieldName, fieldValue)
					.orElseGet(() ->
							fail(String.format("No reservation with %s equal to %s found", fieldName, fieldValue)));

			reservation.setId(existingReservation.getId());

			world.setLatestRequestResult(
					mvc.perform(
							put("/v1/reservations/" + existingReservation.getId())
									.content(objectMapper.writeValueAsString(reservation))
									.contentType(MediaType.APPLICATION_JSON_VALUE)));
		});

		When("I update the reservation with ID equal to {int}:",
				(Integer reservationId, DataTable reservationTable) -> {

			Reservation reservation =
					buildReservation(reservationTable.asMap(String.class, String.class));

			world.setLatestRequestResult(
					mvc.perform(
							put("/v1/reservations/" + reservationId.longValue())
									.content(objectMapper.writeValueAsString(reservation))
									.contentType(MediaType.APPLICATION_JSON_VALUE)));
		});

		When("I list reservations", () -> {
			world.setLatestRequestResult(
					mvc.perform(
							get("/v1/reservations/")
									.accept(MediaType.APPLICATION_JSON_VALUE)));
		});

		When("I cancel the reservation with {string} equal to {string}",
				(String fieldName, String fieldValue) -> {

			Reservation existingReservation = findReservation(fieldName, fieldValue)
					.orElseGet(() ->
							fail(String.format("No reservation with %s equal to %s found", fieldName, fieldValue)));

			world.setLatestRequestResult(
					mvc.perform(
							delete("/v1/reservations/" + existingReservation.getId())
									.accept(MediaType.APPLICATION_JSON_VALUE)));

		});

		When("I cancel reservation with ID {int}", (Integer reservationId) -> {
			world.setLatestRequestResult(
					mvc.perform(
							delete("/v1/reservations/" + reservationId)
									.accept(MediaType.APPLICATION_JSON_VALUE)));
		});

		Then("the following reservations are listed:", (DataTable reservationsTable) -> {
			List<Reservation> expectedReservations = reservationsTable.asMaps().stream()
					.map(ReservationUtil::buildReservation)
					.collect(toList());

			List<Reservation> actualReservations =
					objectMapper.readValue(
							world.getLatestRequestResult().andReturn()
									.getResponse()
									.getContentAsString(),
							new TypeReference<>() {	});

			assertThat(actualReservations).hasSize(expectedReservations.size());

			IntStream.range(0, expectedReservations.size())
					.forEach(i -> assertReservation(actualReservations.get(i), expectedReservations.get(i)));
		});

		Then("there is no reservation with {string} equal to {string}",
				(String fieldName, String fieldValue) -> {

			findReservation(fieldName, fieldValue)
					.ifPresent(reservation -> fail(String.format("reservation with %s equal to %s must not exist", fieldName, fieldValue)));

		});

	}

	private Optional<Reservation> findReservation(String fieldName, String fieldValue) {
		return reservationRepository.findAll().stream()
				.filter(existing -> matches(existing, fieldName, fieldValue))
				.findAny();
	}

	private boolean matches(Reservation existingReservation, String fieldName, String fieldValue) {
		if (fieldName.equals("email")) {
			return existingReservation.getEmail().equals(fieldValue);
		}
		return false;
	}

}
