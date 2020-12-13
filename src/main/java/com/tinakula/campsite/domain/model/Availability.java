package com.tinakula.campsite.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class Availability {

	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
}
