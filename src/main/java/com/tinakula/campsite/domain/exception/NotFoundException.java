package com.tinakula.campsite.domain.exception;

public class NotFoundException extends RuntimeException{
	@Override
	public String getMessage() {
		return "reservation not found";
	}
}
