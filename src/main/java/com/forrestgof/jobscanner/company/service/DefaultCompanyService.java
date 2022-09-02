package com.forrestgof.jobscanner.company.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.forrestgof.jobscanner.common.exception.CustomException;
import com.forrestgof.jobscanner.common.exception.ErrorCode;
import com.forrestgof.jobscanner.company.domain.Company;
import com.forrestgof.jobscanner.company.repository.CompanyRepository;
import com.forrestgof.jobscanner.company.util.dto.NpsBassDto;
import com.forrestgof.jobscanner.company.util.dto.NpsBassResponse;
import com.forrestgof.jobscanner.company.util.dto.NpsDetailDto;
import com.forrestgof.jobscanner.company.util.nps.NpsApiExplorer;
import com.forrestgof.jobscanner.jobposting.util.dto.GoogleJobDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCompanyService implements CompanyService {

	private final CompanyRepository companyRepository;
	private final NpsApiExplorer npsApiExplorer;

	@Override
	@Transactional
	public Long createFromGoogleJob(GoogleJobDto googleJobDto) {
		Assert.notNull(googleJobDto, "Google Job Dto must be provided");

		String googleName = googleJobDto.companyName();
		validateCompany(googleName);

		Company company = getCompanyFromNps(googleName)
			.orElse(createDefaultCompany(googleName));
		company.setThumbnailUrl(googleJobDto.thumbnail());

		return companyRepository.findByUniqueKey(company.getUniqueKey())
			.orElseGet(() -> companyRepository.save(company)).getId();
	}

	@Override
	public Company findOne(Long id) {
		return companyRepository.findById(id).orElseThrow();
	}

	@Override
	public Company findByGoogleName(String googleName) {
		return companyRepository.findByGoogleName(googleName)
			.orElseThrow(() -> new CustomException(ErrorCode.ALREADY_EXIST_COMPANY));
	}

	@Override
	public Optional<Company> getCompanyFromNps(String companyName) {
		NpsBassResponse response = npsApiExplorer.getBass(companyName);

		if (response.getLength() == 0) {
			return Optional.empty();
		}

		NpsDetailDto npsDetailDto = getMostRelevantData(response);
		Company company = npsDetailDto.toCompany();
		company.setGoogleName(companyName);

		return Optional.of(company);
	}

	@Override
	public boolean existsByGoogleName(String googleName) {
		return companyRepository.existsByGoogleName(googleName);
	}

	private void validateCompany(String googleName) {
		if (existsByGoogleName(googleName)) {
			throw new CustomException(ErrorCode.ALREADY_EXIST_COMPANY);
		}
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
			.googleName(name)
			.build();
	}
}
