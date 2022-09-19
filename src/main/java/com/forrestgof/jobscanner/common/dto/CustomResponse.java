package com.forrestgof.jobscanner.common.dto;

import com.forrestgof.jobscanner.common.exception.CustomException;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomResponse<T> {

	boolean status;
	String message;
	T data;

	public static <T> CustomResponse<T> success(T data) {
		CustomResponse<T> customResponse = new CustomResponse<>();

		customResponse.status = true;
		customResponse.data = data;

		return customResponse;
	}

	public static CustomResponse<?> error(Exception e) {
		CustomResponse<?> customResponse = new CustomResponse<>();

		customResponse.status = false;
		customResponse.message = e.getClass().getSimpleName();

		return customResponse;
	}

	public static CustomResponse<?> customError(CustomException e) {
		CustomResponse<?> customResponse = new CustomResponse<>();

		customResponse.status = false;
		customResponse.message = e.getClass().getSimpleName() + ": " + e.getMessage();

		return customResponse;
	}
}

