package com.forrestgof.jobscanner.common.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import com.forrestgof.jobscanner.common.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
	private boolean success;
	private T data;
	private ErrorCode error;

	public ApiResponse(@Nullable T data) {
		this.success = true;
		this.data = data;
		this.error = null;
	}

	static <T> ResponseEntity toResponseEntity(T data) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.builder()
				.data(data)
				.success(true)
				.build());
	}

	static <T> ResponseEntity toResponseEntity(HttpStatus httpStatus, T data) {
		return ResponseEntity.status(httpStatus)
			.body(ApiResponse.builder()
				.data(data)
				.success(true)
				.build());
	}

	static ResponseEntity toResponseEntity(ErrorCode e) {
		return ResponseEntity.status(e.getHttpStatus())
			.body(ApiResponse.builder()
				.data(null)
				.success(false)
				.error(e)
				.build());
	}

}
