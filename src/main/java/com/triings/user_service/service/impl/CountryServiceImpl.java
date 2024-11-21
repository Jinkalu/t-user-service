package com.triings.user_service.service.impl;

import com.triings.trringscommon.entity.Country;
import com.triings.user_service.repository.CountryRepository;
import com.triings.user_service.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.triings.trringscommon.enums.CountryStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public boolean isCountryValid(Long countryId) {
        return countryRepository.existsById(countryId);
    }

    @Override
    public Country getCountryById(Long countryId) {
        return countryRepository.findByIdAndStatus(countryId, ACTIVE)
                .orElseThrow();
    }
}
