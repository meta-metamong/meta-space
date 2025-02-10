package com.metamong.mt.domain.facility.dto.request;

import java.util.List;

import com.metamong.mt.domain.facility.model.Zone;
import com.metamong.mt.global.constant.BooleanAlt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ZoneRegistrationRequestDto {
    private int zoneNo;
    private String zoneName;
    private int maxUserCount;
    private BooleanAlt isSharedZone;
    private int hourlyRate;
    private List<ImageRequestDto> images;
    
    public Zone toEntity(Long fctId) {
        return Zone.builder()
                .zoneName(this.zoneName)
                .fctId(fctId)
                .isSharedZone(this.isSharedZone)
                .hourlyRate(this.hourlyRate)
                .maxUserCount(this.maxUserCount)
                .build();
    }
}
