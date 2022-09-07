package com.forrestgof.jobscanner.mail.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailDto {
	String address;
	String title;
	String content;
}
