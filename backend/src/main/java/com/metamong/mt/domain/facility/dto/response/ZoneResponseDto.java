package com.metamong.mt.domain.facility.dto.response;

import java.util.List;

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
public class ZoneResponseDto {
    private Long zoneId;
    private String zoneName;
    private int maxUserCount;
    private int hourlyRate;
    private BooleanAlt isSharedZone;
    private List<ZoneImageResponseDto> zoneImgs;
}
