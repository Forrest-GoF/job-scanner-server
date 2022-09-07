package com.forrestgof.jobscanner.common.exception;

import lombok.Getter;

@Getter
public class UndefinedException extends RuntimeException {

	public UndefinedException() {
		super();
	}

	public UndefinedException(String message) {
		super(message);
	}

}
