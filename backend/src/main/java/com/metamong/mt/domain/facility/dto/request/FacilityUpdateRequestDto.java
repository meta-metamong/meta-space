package com.metamong.mt.domain.facility.dto.request;

import java.time.LocalTime;

import com.metamong.mt.domain.facility.model.FacilityState;
import com.metamong.mt.global.apispec.CommonListUpdateRequestDto;
import com.metamong.mt.global.constant.BooleanAlt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class FacilityUpdateRequestDto {
    private String fctName;
    private String fctPostalCode;
    private String fctAddress;
    private String fctDetailAddress;
    private String fctTel;
    private String catId;
    private String fctGuide;
    private BooleanAlt isOpenOnHolidays;
    private LocalTime fctOpenTime;
    private LocalTime fctCloseTime;
    private Integer unitUsageTime;
    private FacilityState fctState;
    private Double fctLatitude;
    private Double fctLongitude;
    // TODO: image
    private CommonListUpdateRequestDto<ZoneUpdateRequestDto, Long> zones;
    private CommonListUpdateRequestDto<String, Long> addinfos;
}
