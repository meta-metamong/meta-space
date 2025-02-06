package com.metamong.mt.domain.facility.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class FacilityRegistrationResponseDto {
    private final Long generatedId;
    private final List<ImageUploadUrlResponseDto> fctImageUploadUrls;
    private final List<ZoneImageUploadUrlResponseDto> zoneImageUploadUrls;
}
