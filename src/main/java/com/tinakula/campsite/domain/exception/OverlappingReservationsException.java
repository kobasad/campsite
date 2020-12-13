package com.tinakula.campsite.domain.exception;

public class OverlappingReservationsException extends RuntimeException {
	@Override
	public String getMessage() {
		return "reservation overlaps with an existing one";
	}
}
