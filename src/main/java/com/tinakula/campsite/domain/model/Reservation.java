package com.tinakula.campsite.domain.model;

import com.tinakula.campsite.domain.exception.EarlyReservationException;
import com.tinakula.campsite.domain.exception.LateReservationException;
import com.tinakula.campsite.domain.exception.TooLongException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.Period;
import java.time.ZonedDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

	public static final Period MAX_DURATION = Period.ofDays(3);

	private static final Period MINIMUM_ADVANCE = Period.ofDays(1);
	private static final Period MAXIMUM_ADVANCE = Period.ofMonths(1);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "serial")
	private Long id;

	@NotNull(message = "arrival date is mandatory")
	private ZonedDateTime arrivalDate;

	@NotNull(message = "departure date is mandatory")
	private ZonedDateTime departureDate;

	@NotNull(message = "email is mandatory")
	private String email;

	@NotNull(message = "full name is mandatory")
	private String fullName;

	private ZonedDateTime createdAt;

	public Period duration() {
		return Period.between(arrivalDate.toLocalDate(), departureDate.toLocalDate());
	}

	public boolean isTooLate(ZonedDateTime currentTime) {
		return currentTime.plus(MINIMUM_ADVANCE).isAfter(arrivalDate);
	}

	public boolean isTooEarly(ZonedDateTime currentTime) {
		return arrivalDate.isAfter(currentTime.plus(MAXIMUM_ADVANCE));
	}

	private boolean isTooLong() {
		return MAX_DURATION.minus(duration()).isNegative();
	}

	public void validate(ZonedDateTime currentTime) {
		if (isTooLate(currentTime)) {
			throw new LateReservationException();
		} else if (isTooEarly(currentTime)) {
			throw new EarlyReservationException();
		} else if (isTooLong()) {
			throw new TooLongException();
		}
	}
}
