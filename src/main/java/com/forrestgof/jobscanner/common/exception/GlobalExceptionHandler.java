package com.forrestgof.jobscanner.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.forrestgof.jobscanner.common.util.CustomResponse;

import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity customExceptionHandle(CustomException e) {
		log.error(e);
		return CustomResponse.error(e.getErrorCode());
	}

	@ExceptionHandler(UndefinedException.class)
	public ResponseEntity undefinedExceptionHandle(UndefinedException e) {
		log.error(e);
		return CustomResponse.error(e.getMessage());
	}

	@ExceptionHandler
	public ResponseEntity unexpectedExceptionHandle(Exception e) {
		log.error(e);
		return CustomResponse.error(ErrorCode.UNEXPECTED_EXCEPTION);
	}
}
