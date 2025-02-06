package com.metamong.mt.domain.facility.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class ZoneImageUploadUrlResponseDto {
    private final int zoneNo;
    private final List<ImageUploadUrlResponseDto> uploadUrls;
}
