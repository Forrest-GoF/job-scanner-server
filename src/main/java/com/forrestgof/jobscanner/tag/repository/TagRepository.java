package com.forrestgof.jobscanner.tag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.tag.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

	Optional<Tag> findByName(String name);

	boolean existsByName(String name);
}
