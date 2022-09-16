package com.forrestgof.jobscanner.company.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.company.domain.Company;
import com.forrestgof.jobscanner.company.exception.CompanyCustomException;
import com.forrestgof.jobscanner.company.repository.CompanyRepository;
import com.forrestgof.jobscanner.company.util.dto.NpsBassDto;
import com.forrestgof.jobscanner.company.util.dto.NpsBassResponse;
import com.forrestgof.jobscanner.company.util.dto.NpsDetailDto;
import com.forrestgof.jobscanner.company.util.nps.NpsApiExplorer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCompanyService implements CompanyService {

	private final CompanyRepository companyRepository;
	private final NpsApiExplorer npsApiExplorer;

	@Override
	@Transactional
	public Company createFrom(String rawName, String thumbnail) {
		if (existsByRawName(rawName)) {
			throw new CompanyCustomException("No Company exists by rawName");
		}

		Company company = getCompanyFromNps(rawName)
			.orElseGet(() -> createDefaultCompany(rawName));

		company.setThumbnailUrl(thumbnail);

		return companyRepository.findByUniqueKey(company.getUniqueKey())
			.orElseGet(() -> companyRepository.save(company));
	}

	@Override
	public Optional<Company> findByRawName(String rawName) {
		return companyRepository.findByRawName(rawName);
	}

	@Override
	public Optional<Company> getCompanyFromNps(String companyName) {
		NpsBassResponse response = npsApiExplorer.getBass(companyName);

		if (response.getLength() == 0) {
			return Optional.empty();
		}

		NpsDetailDto npsDetailDto = getMostRelevantData(response);
		Company company = npsDetailDto.toCompany();
		company.setRawName(companyName);

		return Optional.of(company);
	}

	@Override
	public boolean existsByRawName(String rawName) {
		return companyRepository.existsByRawName(rawName);
	}

	private NpsDetailDto getMostRelevantData(NpsBassResponse response) {
		List<NpsBassDto> npsBassDtoList = response.getData();
		NpsBassDto npsBassDto = npsBassDtoList.get(0);

		NpsDetailDto npsDetailDto = npsApiExplorer.getDetail(npsBassDto);
		npsDetailDto.setSeq(npsBassDto.getSeq());
		npsDetailDto.setUpdatedAt(npsBassDto.getUpdatedAt());

		return npsDetailDto;
	}

	private Company createDefaultCompany(String name) {
		return Company.builder()
			.name(name)
			.rawName(name)
			.build();
	}
}
