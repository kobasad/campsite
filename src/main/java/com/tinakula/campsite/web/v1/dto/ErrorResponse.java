package com.tinakula.campsite.web.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
	@Builder.Default
	List<String> errors = Collections.emptyList();
}
