package com.metamong.mt.domain.facility.dto.response;

import java.time.LocalTime;
import java.util.List;

import com.metamong.mt.domain.facility.model.FacilityState;
import com.metamong.mt.global.constant.BooleanAlt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class FacilityResponseDto {
    private Long fctId;
    private String fctName;
    private String fctPostalCode;
    private String fctAddress;
    private String fctDetailAddress;
    private double fctLatitude;
    private double fctLongitude;
    private int unitUsageTime;
    private String fctTel;
    private String catId;
    private String catName;
    private String fctGuide;
    private BooleanAlt isOpenOnHolidays;
    private LocalTime fctOpenTime;
    private LocalTime fctCloseTime;
    private int unitUsageTime;
    private FacilityState fctState;
    private List<FacilityImageResponseDto> fctImages;
    private List<ZoneResponseDto> zones;
    private List<AdditionalInfoResponseDto> additionalInfos;
}
