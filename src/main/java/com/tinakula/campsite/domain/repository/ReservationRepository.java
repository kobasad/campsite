package com.tinakula.campsite.domain.repository;

import com.tinakula.campsite.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByArrivalDateBetween(ZonedDateTime start, ZonedDateTime end);
}
