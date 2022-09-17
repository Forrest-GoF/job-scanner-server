package com.forrestgof.jobscanner.tag.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.common.dto.CustomResponse;
import com.forrestgof.jobscanner.tag.domain.Tag;
import com.forrestgof.jobscanner.tag.service.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("tags")
@RequiredArgsConstructor
public class TagApiController {

	private final TagService tagService;

	@GetMapping
	public ResponseEntity<CustomResponse> getTags() {
		List<String> tags = tagService.findAll()
			.stream()
			.map(Tag::getName)
			.collect(Collectors.toList());

		return CustomResponse.success(tags);
	}
}
