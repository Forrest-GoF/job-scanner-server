package com.forrestgof.jobscanner.company.exception;

import com.forrestgof.jobscanner.common.exception.CustomException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CompanyCustomException extends CustomException {

	public CompanyCustomException(String message) {
		super(message);
	}
}
