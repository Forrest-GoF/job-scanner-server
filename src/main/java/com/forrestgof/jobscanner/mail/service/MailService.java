package com.forrestgof.jobscanner.mail.service;

import com.forrestgof.jobscanner.mail.dto.MailDto;

public interface MailService {

	void send(MailDto mailDto);
}
