package com.forrestgof.jobscanner.auth.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.forrestgof.jobscanner.auth.dto.KakaoUserResponse;

@Repository
public class MemoryKakaoRepsitory {

	private Map<String, KakaoUserResponse> store = new HashMap<>();

	public KakaoUserResponse save(KakaoUserResponse kakaoUserResponse) {
		store.put(kakaoUserResponse.getId(), kakaoUserResponse);
		return kakaoUserResponse;
	}

	public Optional<KakaoUserResponse> findById(String id) {
		return Optional.ofNullable(store.get(id));
	}
}
