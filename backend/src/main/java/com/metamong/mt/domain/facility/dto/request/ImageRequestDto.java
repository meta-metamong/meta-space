package com.metamong.mt.domain.facility.dto.request;

import com.metamong.mt.global.file.FileType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ImageRequestDto {
    private FileType fileType;
    private int order;
}
