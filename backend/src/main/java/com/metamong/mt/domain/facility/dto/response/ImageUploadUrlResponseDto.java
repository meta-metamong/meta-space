package com.metamong.mt.domain.facility.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ImageUploadUrlResponseDto {
    private final int order;
    private final String uploadUrl;
}
