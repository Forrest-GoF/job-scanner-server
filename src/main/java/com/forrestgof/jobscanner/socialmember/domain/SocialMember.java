package com.forrestgof.jobscanner.socialmember.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.forrestgof.jobscanner.common.entity.BaseTimeEntity;
import com.forrestgof.jobscanner.member.domain.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialMember extends BaseTimeEntity {

	@Id
	@GeneratedValue
	@Column(name = "social_member_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	private String email;

	@Enumerated(value = EnumType.STRING)
	private SocialType socialType;

	@Builder
	public SocialMember(
		Member member,
		String email,
		SocialType socialType
	) {
		this.member = member;
		this.email = email;
		this.socialType = socialType;
	}
}
