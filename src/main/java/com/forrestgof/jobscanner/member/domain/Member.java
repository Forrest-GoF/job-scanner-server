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
	@Column(name = "member_id")
	private Long id;

	@Column(unique = true)
	private String email;

	private String password;

	private String nickname;

	private String imageUrl;

	@Builder
	public Member(
		String email,
		String password,
		String nickname,
		String imageUrl
	) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
	}
}
