package com.metamong.mt.domain.facility.dto.mapper;

import com.metamong.mt.domain.facility.dto.request.ZoneUpdateRequestDto;
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
public class ZoneUpdateMapperDto {
    private Long zoneId;
    private String zoneName;
    private Integer maxUserCount;
    private BooleanAlt isSharedZone;
    private Integer hourlyRate;
    
    public static ZoneUpdateMapperDto of(Long zoneId, ZoneUpdateRequestDto dto) {
        return ZoneUpdateMapperDto.builder()
                .zoneId(zoneId)
                .zoneName(dto.getZoneName())
                .maxUserCount(dto.getMaxUserCount())
                .isSharedZone(dto.getIsSharedZone())
                .hourlyRate(dto.getHourlyRate())
                .build();
    }
}
