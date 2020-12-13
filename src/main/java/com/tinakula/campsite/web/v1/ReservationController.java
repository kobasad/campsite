package com.tinakula.campsite.web.v1;

import com.tinakula.campsite.domain.model.Campsite;
import com.tinakula.campsite.domain.model.Reservation;
import com.tinakula.campsite.web.v1.dto.ReservationResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/reservations")
public class ReservationController {

	private final Campsite campsite;

	public ReservationController(Campsite campsite) {
		this.campsite = campsite;
	}

	@GetMapping
	public List<Reservation> listExistingReservations() {
		return campsite.findAllReservations();
	}

	@PostMapping
	public ReservationResponse createReservation(@RequestBody @Valid Reservation reservation) {
		Reservation result = campsite.createReservation(reservation);
		return ReservationResponse.builder()
				.id(result.getId())
				.build();
	}

	@PutMapping("{reservationId}")
	public ReservationResponse updateReservation(
			@PathVariable Long reservationId,
			@RequestBody @Valid Reservation reservation) {

		Reservation result = campsite.updateReservation(reservationId, reservation);
		return ReservationResponse.builder()
				.id(result.getId())
				.build();
	}

	@DeleteMapping ("{reservationId}")
	public void cancelReservation(@PathVariable Long reservationId) {

		campsite.cancelReservation(reservationId);
	}

}

