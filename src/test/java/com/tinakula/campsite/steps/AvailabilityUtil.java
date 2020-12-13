package com.tinakula.campsite.steps;

import com.tinakula.campsite.domain.model.Availability;

import java.time.ZonedDateTime;
import java.util.Map;

public class AvailabilityUtil {

	public static Availability buildAvailability(Map<String, String> availabilitiesMap) {
		return Availability.builder()
				.startDate(availabilitiesMap.getOrDefault("start date", "").isBlank() ?
						null : ZonedDateTime.parse(availabilitiesMap.get("start date")))
				.endDate(availabilitiesMap.getOrDefault("end date", "").isBlank() ?
						null : ZonedDateTime.parse(availabilitiesMap.get("end date")))
				.build();
	}
}
