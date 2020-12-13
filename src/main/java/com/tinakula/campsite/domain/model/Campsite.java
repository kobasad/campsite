package com.tinakula.campsite.domain.model;

import com.tinakula.campsite.domain.exception.NotFoundException;
import com.tinakula.campsite.domain.exception.OverlappingReservationsException;
import com.tinakula.campsite.domain.repository.ReservationRepository;
import com.tinakula.campsite.domain.service.TimeService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class Campsite {

	private final ReservationRepository repository;
	private final TimeService timeService;
	private final TransactionTemplate transactionTemplate;

	public Campsite(
			ReservationRepository repository,
			TimeService timeService,
			PlatformTransactionManager platformTransactionManager) {

		this.repository = repository;
		this.timeService = timeService;
		this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
	}

	public List<Reservation> findAllReservations() {
		return repository.findAll();
	}

	public Reservation createReservation(Reservation reservation) {

		reservation.validate(timeService.currentTime());

		try {
			reservation.setCreatedAt(timeService.currentTime());
			return repository.save(reservation);
		} catch (DataIntegrityViolationException e) {
			throw new OverlappingReservationsException();
		}
	}

	public Reservation updateReservation(Long reservationId, Reservation reservation) {

		reservation.validate(timeService.currentTime());

		try {
			return transactionTemplate.execute(transactionStatus ->
					repository.findById(reservationId)
							.map(stored -> updateFields(stored, reservation))
							.orElseThrow(NotFoundException::new));
		} catch (DataIntegrityViolationException e) {
			throw new OverlappingReservationsException();
		}
	}

	private Reservation updateFields(Reservation stored, Reservation reservation) {
		stored.setArrivalDate(reservation.getArrivalDate());
		stored.setDepartureDate(reservation.getDepartureDate());
		stored.setEmail(reservation.getEmail());
		stored.setFullName(reservation.getFullName());
		return stored;
	}

	public List<Availability> findAvailabilities(ZonedDateTime start, ZonedDateTime end) {

		List<Reservation> reservations =
				repository.findByArrivalDateBetween(
						start.minus(Reservation.MAX_DURATION),
						end);

		AtomicReference<ZonedDateTime> dateCursor = new AtomicReference<>(start);

		List<Availability> result = new LinkedList<>();

		reservations.forEach(reservation -> {

			if (reservation.getDepartureDate().toInstant().isBefore(dateCursor.get().toInstant())) {
				// skip this reservation, it ends before the search period
			} else {
				if (dateCursor.get().toInstant().equals(reservation.getArrivalDate().toInstant())) {
					// skip this reservation, it starts at the current cursor
				} else if (reservation.getArrivalDate().toInstant().isAfter(dateCursor.get().toInstant())) {
					// there are free days between the current cursor and next reservation's start,
					// so create availability
					result.add(
							Availability.builder()
									.startDate(dateCursor.get())
									.endDate(reservation.getArrivalDate())
									.build());
				}
				dateCursor.set(reservation.getDepartureDate());
			}
		});

		if (dateCursor.get().toInstant().isBefore(end.toInstant())) {
			result.add(
					Availability.builder()
							.startDate(dateCursor.get())
							.endDate(end)
							.build());
		}

		return result;
	}

	public void cancelReservation(Long reservationId) {
		repository.findById(reservationId)
				.ifPresentOrElse(
						reservation -> repository.deleteById(reservationId),
						() -> { throw new NotFoundException(); });
	}
}
