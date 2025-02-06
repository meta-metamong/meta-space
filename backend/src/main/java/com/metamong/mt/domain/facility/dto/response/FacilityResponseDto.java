package com.metamong.mt.domain.facility.dto.response;

import java.time.LocalTime;
import java.util.List;

import com.metamong.mt.domain.facility.model.FacilityState;

import lombok.Builder;

@Builder
public record FacilityResponseDto(
        Long fctId,
        String fctName,
        String fctPostalCode,
        String fctAddress,
        String fctDetailAddress,
        double fctLatitude,
        double fctLongitude,
        String fctTel,
        String catId,
        String catName,
        String fctGuide,
        boolean isOpenOnHolidays,
        LocalTime fctOpenTime,
        LocalTime fctClosetime,
        FacilityState fctState,
        List<FacilityImageResponseDto> fctImages,
        List<ZoneResponseDto> zones,
        List<String> additionalInfos
) { 
}
