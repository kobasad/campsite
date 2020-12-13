package com.tinakula.campsite.domain.exception;

public class LateReservationException extends RuntimeException {
	@Override
	public String getMessage() {
		return "reservation can be created at least 1 day before arrival time";
	}
}
