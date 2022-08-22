package com.forrestgof.jobscanner.jobposting.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.jobposting.domain.Tag;
import com.forrestgof.jobscanner.jobposting.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

	private final TagRepository tagRepository;

	@Transactional
	public Tag save(Tag tag) {
		return tagRepository.save(tag);
	}

	public Tag findByName(String name) {
		return tagRepository.findByName(name).orElseThrow();
	}

	public List<Tag> findAll() {
		return tagRepository.findAll();
	}

	public boolean existsByName(String name) {
		return tagRepository.existsByName(name);
	}
}
