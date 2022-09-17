package com.forrestgof.jobscanner.member.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.forrestgof.jobscanner.common.entity.BaseTimeEntity;
import com.forrestgof.jobscanner.duty.domain.MemberDuty;

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

	private boolean isAuthenticatedEmail;

	@OneToMany(mappedBy = "member")
	private final List<MemberTag> memberTags = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private final List<MemberDuty> memberDuties = new ArrayList<>();

	@Builder
	public Member(
		String email,
		String password,
		String nickname,
		String imageUrl,
		boolean isAuthenticatedEmail
	) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
		this.isAuthenticatedEmail = isAuthenticatedEmail;
	}

	public void authenticateEmail() {
		this.isAuthenticatedEmail = true;
	}

	public void updateMember(
		@NotNull String nickname,
		String imageUrl
	) {
		this.nickname = nickname;
		this.imageUrl = imageUrl;
	}

	public void updateMemberTags(List<MemberTag> memberTags) {

		this.memberTags.clear();
		this.memberTags.addAll(memberTags);
	}

	public void updateMemberDuties(List<MemberDuty> memberDuties) {
		this.memberDuties.clear();
		this.memberDuties.addAll(memberDuties);
	}
}
