package com.metamong.mt.domain.facility.dto.mapper;

import java.time.LocalTime;

import com.metamong.mt.domain.facility.dto.request.FacilityUpdateRequestDto;
import com.metamong.mt.domain.facility.model.FacilityState;
import com.metamong.mt.global.constant.BooleanAlt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class FacilityUpdateMapperDto {
    private Long fctId;
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
    
    public static FacilityUpdateMapperDto of(Long fctId, FacilityUpdateRequestDto dto) {
        return FacilityUpdateMapperDto.builder()
                .fctId(fctId)
                .fctName(dto.getFctName())
                .fctPostalCode(dto.getFctPostalCode())
                .fctAddress(dto.getFctAddress())
                .fctDetailAddress(dto.getFctDetailAddress())
                .fctTel(dto.getFctTel())
                .catId(dto.getCatId())
                .fctGuide(dto.getFctGuide())
                .isOpenOnHolidays(dto.getIsOpenOnHolidays())
                .fctOpenTime(dto.getFctOpenTime())
                .fctCloseTime(dto.getFctCloseTime())
                .unitUsageTime(dto.getUnitUsageTime())
                .fctState(dto.getFctState())
                .fctLatitude(dto.getFctLatitude())
                .fctLongitude(dto.getFctLongitude())
                .build();
    }
}
