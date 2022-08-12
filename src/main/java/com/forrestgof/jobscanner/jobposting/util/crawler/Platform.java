package com.forrestgof.jobscanner.jobposting.util.crawler;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Platform {
	WANTED("원티드", "/wanted"),
	JUMPIT("점핏", "/jumpit"),
	ROCKETPUCH("로켓펀치", "/jumpit");
	// JOBPLANET("잡플래닛", "/jobplanet");

	private final String name;
	private final String url;

	public static Optional<Platform> of(String platform) {
		return Arrays.stream(Platform.values())
			.filter(r -> r.getName().equals(platform))
			.findFirst();
	}
}
