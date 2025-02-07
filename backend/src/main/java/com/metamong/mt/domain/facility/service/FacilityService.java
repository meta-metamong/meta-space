package com.metamong.mt.domain.facility.service;

import com.metamong.mt.domain.facility.dto.request.FacilityRegistrationRequestDto;
import com.metamong.mt.domain.facility.dto.response.FacilityRegistrationResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityResponseDto;

public interface FacilityService {

    FacilityRegistrationResponseDto registerFacility(FacilityRegistrationRequestDto dto);
    
    FacilityResponseDto getFacility(Long facilityId);
}
