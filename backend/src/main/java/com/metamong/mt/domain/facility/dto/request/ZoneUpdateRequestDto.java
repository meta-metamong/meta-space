package com.metamong.mt.domain.facility.dto.request;

import com.metamong.mt.global.constant.BooleanAlt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ZoneUpdateRequestDto {
    private String zoneName;
    private Integer maxUserCount;
    private BooleanAlt isSharedZone;
    private Integer hourlyRate;
}
