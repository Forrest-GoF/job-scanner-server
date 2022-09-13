package com.forrestgof.jobscanner.mail.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.forrestgof.jobscanner.common.config.properties.DomainProperties;
import com.forrestgof.jobscanner.common.exception.UndefinedException;
import com.forrestgof.jobscanner.mail.dto.MailDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMailService implements MailService {

	private final JavaMailSender emailSender;
	private final TemplateEngine templateEngine;
	private final DomainProperties domainProperties;

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
			throw new UndefinedException("Mail Error");
		}

		emailSender.send(message);
		log.info("mail send complete.");
	}

	public void sendAuthenticationMail(String email, String newAppToken) {
		Context context = new Context();
		context.setVariable("link",
			domainProperties.apiServer() + "/auth/mail/authenticate/" + email + "/" + newAppToken);
		String authenticationMailTemplate = templateEngine.process("authenticationMailTemplate", context);

		send(MailDto.builder()
			.title("[JobScanner]가입을 환영합니다.")
			.address(email)
			.content(authenticationMailTemplate)
			.build()
		);
	}
}
