package com.metamong.mt.domain.facility.dto.request;

import java.util.List;

import com.metamong.mt.domain.facility.model.Zone;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ZoneRegistrationRequestDto {
    private int zoneNo;
    private String zoneName;
    private int maxUserCount;
    private int isSharedZone;
    private int hourlyRate;
    private List<ImageRequestDto> images;
    
    public Zone toEntity() {
        return Zone.builder()
                .zoneName(this.zoneName)
                .isSharedZone(this.isSharedZone)
                .hourlyRate(this.hourlyRate)
                .build();
    }
}
