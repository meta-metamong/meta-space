package com.metamong.mt.domain.facility.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class ZoneImageResponseDto {
    private final int zoneImgDisplayOrder;
    private final String zoneImgUrl;
}
