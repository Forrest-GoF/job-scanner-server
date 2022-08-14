package com.forrestgof.jobscanner.session.domain;

import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class Session extends BaseTimeEntity {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(unique = true)
	private String appTokenUuid;

	private String refreshTokenUuid;

	@Builder
	public Session(Member member, String appTokenUuid, String refreshTokenUuid) {
		this.member = member;
		this.appTokenUuid = appTokenUuid;
		this.refreshTokenUuid = refreshTokenUuid;
	}
}
