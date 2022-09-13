package com.forrestgof.jobscanner.member.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.forrestgof.jobscanner.duty.domain.Duty;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.domain.ObjectiveCompany;
import com.forrestgof.jobscanner.tag.domain.Tag;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponse {

	private String email;
	private String nickname;
	private String imageUrl;

	private List<String> tags;
	private List<String> duties;

	private int objectiveEmployeeCount;
	private long objectiveSalary;

	public void updateMember(Member member) {
		this.email = member.getEmail();
		this.nickname = member.getNickname();
		this.imageUrl = member.getImageUrl();
	}

	public void updateTags(List<Tag> tags) {
		this.tags = tags.stream()
			.map(Tag::getName)
			.collect(Collectors.toList());
	}

	public void updateDuties(List<Duty> duties) {
		this.duties = duties.stream()
			.map(Duty::getName)
			.collect(Collectors.toList());
	}

	public void updateObjectiveCompany(ObjectiveCompany objectiveCompany) {
		this.objectiveEmployeeCount = objectiveCompany.getEmployeeCount();
		this.objectiveSalary = objectiveCompany.getSalary();
	}
}
