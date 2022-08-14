package com.forrestgof.jobscanner.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.forrestgof.jobscanner.common.exception.ErrorCode;

public class CustomResponse {

	public static <T> ResponseEntity<T> success() {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	public static <T> ResponseEntity<T> success(T data) {
		return ResponseEntity.status(HttpStatus.OK).body(data);
	}

	public static ResponseEntity<String> error(ErrorCode e) {
		return ResponseEntity.status(e.getStatus()).body(e.getMessage());
	}
}
