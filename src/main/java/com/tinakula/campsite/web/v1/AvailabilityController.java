package com.tinakula.campsite.web.v1;

import com.tinakula.campsite.domain.model.Availability;
import com.tinakula.campsite.domain.model.Campsite;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Period;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("v1/availabilities")
public class AvailabilityController {

	private final Campsite campsite;

	public AvailabilityController(Campsite campsite) {
		this.campsite = campsite;
	}

	@GetMapping
	public List<Availability> findAvailabilities(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime start,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime end) {

		return campsite.findAvailabilities(
				start,
				end != null ? end : start.plus(Period.ofMonths(1))
		);
	}
}
