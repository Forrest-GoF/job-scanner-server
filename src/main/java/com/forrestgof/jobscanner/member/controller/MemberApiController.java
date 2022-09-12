package com.forrestgof.jobscanner.member.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.auth.jwt.JwtHeaderUtil;
import com.forrestgof.jobscanner.auth.service.AuthService;
import com.forrestgof.jobscanner.common.util.CustomResponse;
import com.forrestgof.jobscanner.duty.domain.Duty;
import com.forrestgof.jobscanner.duty.service.MemberDutyService;
import com.forrestgof.jobscanner.member.controller.dto.MemberResponse;
import com.forrestgof.jobscanner.member.domain.Member;
import com.forrestgof.jobscanner.member.domain.ObjectiveCompany;
import com.forrestgof.jobscanner.member.service.MemberService;
import com.forrestgof.jobscanner.member.service.ObjectiveCompanyService;
import com.forrestgof.jobscanner.tag.domain.Tag;
import com.forrestgof.jobscanner.tag.service.MemberTagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberApiController {

	private final AuthService authService;
	private final MemberTagService memberTagService;
	private final MemberDutyService memberDutyService;
	private final ObjectiveCompanyService objectiveCompanyService;

	@GetMapping("")
	public ResponseEntity<MemberResponse> getMember(HttpServletRequest request) {
		String appToken = JwtHeaderUtil.getAccessToken(request);

		Member member = authService.getMemberFromAppToken(appToken);
		List<Tag> tags = memberTagService.findTagsFromMember(member);
		List<Duty> duties = memberDutyService.findDutiesFromMember(member);
		Optional<ObjectiveCompany> objectiveCompany = objectiveCompanyService.findByMember(member);

		MemberResponse memberResponse = new MemberResponse();
		memberResponse.updateMember(member);
		memberResponse.updateTags(tags);
		memberResponse.updateDuties(duties);
		objectiveCompany.ifPresent(memberResponse::updateObjectiveCompany);

		return CustomResponse.success(memberResponse);
	}
}
