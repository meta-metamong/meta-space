package com.metamong.mt.domain.facility.service;

import java.util.List;

import com.metamong.mt.domain.facility.dto.request.FacilityListRequestDto;
import com.metamong.mt.domain.facility.dto.request.FacilityRegistrationRequestDto;
import com.metamong.mt.domain.facility.dto.request.FacilityUpdateRequestDto;
import com.metamong.mt.domain.facility.dto.response.FacilityListOfMemberResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityListResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityRegistrationResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityUpdateResponseDto;

public interface FacilityService {

    FacilityRegistrationResponseDto registerFacility(FacilityRegistrationRequestDto dto);
    
    FacilityResponseDto getFacility(Long facilityId);
    
    FacilityUpdateResponseDto updateFacility(Long fctId, FacilityUpdateRequestDto dto);
    
    void deleteFacility(Long fctId, Long memId);
    
    FacilityListResponseDto getFacilities(FacilityListRequestDto dto);
    
    List<FacilityListOfMemberResponseDto> getFacilityOfMember(Long memId);
    
    Long getMemberIdByZoneId(Long zoneId);
}
