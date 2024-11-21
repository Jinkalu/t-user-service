package com.triings.user_service.service;

import com.triings.trringscommon.entity.Country;

public interface CountryService {
    boolean isCountryValid(Long countryId);
    Country getCountryById(Long countryId);
}
