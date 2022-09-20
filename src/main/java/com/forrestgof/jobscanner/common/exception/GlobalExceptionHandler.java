package com.forrestgof.jobscanner.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
	public ResponseEntity<CustomResponse<?>> handleCustomException(CustomException e) {
		log.error("Custom Exception", e);

		return ResponseEntity.status(e.getHttpStatus())
			.body(CustomResponse.customError(e));
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public CustomResponse<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		log.error("Http Request Method Not Supported Exception", e);

		return CustomResponse.errorWithMessage("Http request method is not supported");
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CustomResponse<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("MethodArgumentNotValidException", e);

		return CustomResponse.errorWithMessage(
			e.getBindingResult()
				.getAllErrors()
				.get(0)
				.getDefaultMessage());
	}
}
