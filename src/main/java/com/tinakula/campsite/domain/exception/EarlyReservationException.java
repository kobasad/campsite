package com.tinakula.campsite.domain.exception;

public class EarlyReservationException extends RuntimeException {
	@Override
	public String getMessage() {
		return "reservation can be created at most 1 month before arrival time";
	}
}
