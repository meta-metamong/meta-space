package com.metamong.mt.domain.facility.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class FacilityListResponseDto {
    private int page;
    private int pageSize;
    private int totalElementCount;
    private int totalPageCount;
    private boolean isFirst;
    private boolean isLast;
    private List<FacilityListItemResponseDto> facilities;
}
