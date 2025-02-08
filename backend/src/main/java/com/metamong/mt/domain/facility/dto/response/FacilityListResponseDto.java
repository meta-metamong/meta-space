package com.metamong.mt.domain.facility.dto.response;

import java.util.List;

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
public class FacilityListResponseDto {
    private int number;
    private int pageSize;
    private int totalElementCount;
    private int totalPageCount;
    private boolean isFirst;
    private boolean isLast;
    private List<FacilityListItemResponseDto> facilities;
}
