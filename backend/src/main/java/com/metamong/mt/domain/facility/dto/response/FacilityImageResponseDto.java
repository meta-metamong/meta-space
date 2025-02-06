package com.metamong.mt.domain.facility.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class FacilityImageResponseDto {
    private final int fctImgDisplayOrder;
    private final String fctImgUrl;
}
