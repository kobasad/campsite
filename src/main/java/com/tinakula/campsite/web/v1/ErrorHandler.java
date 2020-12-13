package com.tinakula.campsite.web.v1;

import com.tinakula.campsite.domain.exception.EarlyReservationException;
import com.tinakula.campsite.domain.exception.LateReservationException;
import com.tinakula.campsite.domain.exception.NotFoundException;
import com.tinakula.campsite.domain.exception.OverlappingReservationsException;
import com.tinakula.campsite.domain.exception.TooLongException;
import com.tinakula.campsite.web.v1.dto.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@RestControllerAdvice
public class ErrorHandler {
	@ExceptionHandler(OverlappingReservationsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleException(OverlappingReservationsException e) {

		return ErrorResponse.builder()
				.errors(singletonList(e.getMessage()))
				.build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleException(MethodArgumentNotValidException e) {

		return ErrorResponse.builder()
				.errors(e.getFieldErrors().stream()
						.map(DefaultMessageSourceResolvable::getDefaultMessage)
						.collect(toList()))
				.build();
	}

	@ExceptionHandler({EarlyReservationException.class, LateReservationException.class, TooLongException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleException(Exception e) {

		return ErrorResponse.builder()
				.errors(singletonList(e.getMessage()))
				.build();
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleException(NotFoundException e) {

		return ErrorResponse.builder()
				.errors(singletonList(e.getMessage()))
				.build();
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleException(MethodArgumentTypeMismatchException e) {

		return ErrorResponse.builder()
				.errors(singletonList("invalid " + e.getName()))
				.build();
	}
}
