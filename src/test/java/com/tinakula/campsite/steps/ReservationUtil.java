package com.tinakula.campsite.steps;

import com.tinakula.campsite.domain.model.Reservation;

import java.time.ZonedDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationUtil {

	public static Reservation buildReservation(Map<String, String> reservationMap) {
		return Reservation.builder()
				.id(reservationMap.getOrDefault("id", "").isBlank() ? null : Long.parseLong(reservationMap.get("id")))
				.arrivalDate(reservationMap.getOrDefault("arrival date", "").isBlank() ? null : ZonedDateTime.parse(reservationMap.get("arrival date")))
				.departureDate(reservationMap.getOrDefault("departure date", "").isBlank() ? null : ZonedDateTime.parse(reservationMap.get("departure date")))
				.email(reservationMap.getOrDefault("email", "").isBlank() ? null : reservationMap.get("email"))
				.fullName(reservationMap.getOrDefault("full name", "").isBlank() ? null : reservationMap.get("full name"))
				.createdAt(reservationMap.getOrDefault("creation timestamp", "").isBlank() ? null : ZonedDateTime.parse(reservationMap.get("creation timestamp")))
				.build();
	}

	public static boolean haveSameDate(Reservation stored, Reservation expected) {
		return stored.getArrivalDate().toInstant()
				.equals(expected.getArrivalDate().toInstant());
	}

	public static void assertReservation(Reservation stored, Reservation expected) {
		assertThat(stored.getArrivalDate())
				.isEqualTo(expected.getArrivalDate());

		assertThat(stored.getDepartureDate())
				.isEqualTo(expected.getDepartureDate());

		assertThat(stored.getEmail())
				.isEqualTo(expected.getEmail());

		assertThat(stored.getFullName())
				.isEqualTo(expected.getFullName());

		assertThat(stored.getCreatedAt().toInstant())
				.isEqualTo(expected.getCreatedAt().toInstant());
	}
}
