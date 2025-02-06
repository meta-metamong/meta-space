package com.metamong.mt.domain.facility.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Builder
@Getter
@ToString
public class ZoneResponseDto {
    private final Long zoneId;
    private final String zoneName;
    private final int maxUserCount;
    private final int hourlyRate;
    private final boolean isSharedZone;
    private final List<ZoneImageResponseDto> zoneImgs;
}
