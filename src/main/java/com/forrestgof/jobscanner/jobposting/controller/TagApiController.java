package com.forrestgof.jobscanner.jobposting.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.forrestgof.jobscanner.common.util.CustomResponse;
import com.forrestgof.jobscanner.jobposting.domain.Tag;
import com.forrestgof.jobscanner.jobposting.service.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagApiController {

	private final TagService tagService;

	@GetMapping
	@ResponseBody
	public ResponseEntity<List<String>> getTags() {
		List<Tag> all = tagService.findAll();
		List<String> tags = all.stream()
			.map(Tag::getName)
			.collect(Collectors.toList());

		return CustomResponse.success(tags);
	}
}