package com.forrestgof.jobscanner.common.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.forrestgof.jobscanner.common.exception.CustomException;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomResponse {

	boolean status;
	String message;
	Object data;

	public static ResponseEntity<CustomResponse> success() {
		CustomResponse customResponse = CustomResponse.builder()
			.status(true)
			.build();

		return ResponseEntity.status(HttpStatus.OK)
			.body(customResponse);
	}

	public static ResponseEntity<CustomResponse> success(Object data) {
		CustomResponse customResponse = CustomResponse.builder()
			.status(true)
			.data(data)
			.build();

		return ResponseEntity.status(HttpStatus.OK)
			.body(customResponse);
	}

	public static ResponseEntity<CustomResponse> success(Object data, HttpStatus httpStatus) {
		CustomResponse customResponse = CustomResponse.builder()
			.status(true)
			.data(data)
			.build();

		return ResponseEntity.status(httpStatus)
			.body(customResponse);
	}

	public static ResponseEntity<CustomResponse> error(Exception e) {
		CustomResponse customResponse = CustomResponse.builder()
			.status(false)
			.message(e.getMessage())
			.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(customResponse);
	}

	public static ResponseEntity<CustomResponse> customError(CustomException e) {
		CustomResponse customResponse = CustomResponse.builder()
			.status(false)
			.message(e.getClass().getName() + ": " + e.getMessage())
			.build();

		return ResponseEntity.status(e.getHttpStatus())
			.body(customResponse);
	}
}

