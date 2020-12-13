package com.tinakula.campsite.steps;

import com.tinakula.campsite.domain.model.Reservation;
import com.tinakula.campsite.domain.repository.ReservationRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

import java.util.List;

import static com.tinakula.campsite.steps.ReservationUtil.*;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class PersistenceSteps implements En {

	public PersistenceSteps(ReservationRepository reservationRepository) {

		Given("there are some reservations:", (DataTable expectedTable) -> {
			reservationRepository.deleteAll();
			List<Reservation> existingReservations = reservationRepository.saveAll(
					expectedTable.<String, String>asMaps(String.class, String.class).stream()
							.map(ReservationUtil::buildReservation)
							.collect(toList()));
		});

		Given("there are no existing reservations", () ->
				reservationRepository.deleteAll()
		);

		Then("a reservation is created:", (DataTable expectedTable) -> {
			List<Reservation> storedReservations = reservationRepository.findAll();
			assertThat(storedReservations).isNotEmpty();

			Reservation expectedReservation =
					buildReservation(expectedTable.asMap(String.class, String.class));

			Reservation storedReservation = storedReservations.stream()
					.filter(stored -> haveSameDate(stored, expectedReservation))
					.findFirst()
					.orElseGet(() -> fail("Expected reservation was not created"));

			assertReservation(storedReservation, expectedReservation);
		});

		Then("a reservation is updated:", (DataTable expectedTable) -> {
			List<Reservation> storedReservations = reservationRepository.findAll();
			assertThat(storedReservations).isNotEmpty();

			Reservation expectedReservation =
					buildReservation(expectedTable.asMap(String.class, String.class));

			Reservation storedReservation = storedReservations.stream()
					.filter(stored -> haveSameDate(stored, expectedReservation))
					.findFirst()
					.orElseGet(() -> fail("Expected reservation was not created"));

			assertReservation(storedReservation, expectedReservation);
		});

	}
}
