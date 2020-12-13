package com.tinakula.campsite.domain.exception;

public class TooLongException extends RuntimeException {
	@Override
	public String getMessage() {
		return "maximum duration is 3 days";
	}
}
