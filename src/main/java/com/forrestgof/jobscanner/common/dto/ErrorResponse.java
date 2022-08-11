package com.forrestgof.jobscanner.common.dto;

import com.forrestgof.jobscanner.common.exception.ErrorCode;

import lombok.Data;

@Data
public class ErrorResponse {

	private String message;

	private ErrorResponse(ErrorCode errorCode) {
		this.message = errorCode.getMessage();
	}

	public static ErrorResponse of(ErrorCode code) {
		return new ErrorResponse(code);
	}
}
