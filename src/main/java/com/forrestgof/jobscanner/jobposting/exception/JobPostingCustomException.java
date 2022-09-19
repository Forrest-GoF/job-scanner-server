package com.forrestgof.jobscanner.jobposting.exception;

import org.springframework.http.HttpStatus;

import com.forrestgof.jobscanner.common.exception.CustomException;

public class JobPostingCustomException extends CustomException {

	public JobPostingCustomException(String message, HttpStatus httpStatus) {
		super(message, httpStatus);
	}

	public static JobPostingCustomException notfound() {
		return new JobPostingCustomException("Not found Job", HttpStatus.NOT_FOUND);
	}
}
