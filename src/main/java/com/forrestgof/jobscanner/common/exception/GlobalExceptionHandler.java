package com.forrestgof.jobscanner.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.forrestgof.jobscanner.common.dto.CustomResponse;

import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public CustomResponse<?> handleUnExpectedException(Exception e) {
		log.error("UnExpected Exception", e);
		return CustomResponse.error(e);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CustomResponse<?> handleCustomException(CustomException e) {
		log.error("Custom Exception", e);
		return CustomResponse.customError(e);
	}
}
