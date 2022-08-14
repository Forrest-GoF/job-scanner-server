package com.forrestgof.jobscanner.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.forrestgof.jobscanner.common.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String email;

	private String nickname;

	private String imageUrl;

	@Builder
	public Member(String email, String nickname, String imageUrl) {
		this.email = email;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
	}
}
