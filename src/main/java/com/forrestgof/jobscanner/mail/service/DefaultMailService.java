package com.forrestgof.jobscanner.mail.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.mail.dto.MailDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMailService implements MailService {

	private final JavaMailSender emailSender;

	@Async
	public void send(MailDto mailDto) {
		MimeMessage message = emailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom("contact.jobscanner@gmail.com");
			helper.setSubject(mailDto.getTitle());
			helper.setText(mailDto.getContent(), true);
			helper.setTo(mailDto.getAddress());
		} catch (MessagingException e) {
			throw new CustomException(e.getMessage());
		}

		emailSender.send(message);
		log.info("mail send complete.");
	}
}
