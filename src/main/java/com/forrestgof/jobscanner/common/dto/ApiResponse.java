package com.forrestgof.jobscanner.common.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.forrestgof.jobscanner.common.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {
	private boolean success;
	private T data;
	private ErrorResponse error;

	public static <T> ResponseEntity toResponseEntity() {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.builder()
				.success(true)
				.data(null)
				.build());
	}

	public static <T> ResponseEntity toResponseEntity(T data) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.builder()
				.success(true)
				.data(data)
				.build());
	}

	public static <T> ResponseEntity toResponseEntity(HttpStatus httpStatus, T data) {
		return ResponseEntity.status(httpStatus)
			.body(ApiResponse.builder()
				.success(true)
				.data(data)
				.build());
	}

	public static ResponseEntity toResponseEntity(ErrorCode e) {
		return ResponseEntity.status(e.getStatus())
			.body(ApiResponse.builder()
				.success(false)
				.data(null)
				.error(ErrorResponse.of(e))
				.build());
	}

}
