package com.metamong.mt.domain.facility.service;

import com.metamong.mt.domain.facility.dto.request.FacilityRegistrationRequestDto;
import com.metamong.mt.domain.facility.dto.response.FacilityRegistrationResponseDto;

public interface FacilityService {

    FacilityRegistrationResponseDto registerFacility(FacilityRegistrationRequestDto dto);
}
