package com.metamong.mt.domain.facility.dto.request;

import java.util.List;

import com.metamong.mt.domain.facility.dto.constant.Order;
import com.metamong.mt.domain.facility.dto.constant.OrderBy;
import com.metamong.mt.domain.facility.dto.constant.SearchCondition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class FacilityListRequestDto {
    private final Order order;
    private final OrderBy orderBy;
    private final int page;
    private final int pageSize;
    private final Long providerId;
    private final String searchKeyword;
    private final SearchCondition searchCondition;
    private final List<String> catIds;
    private final Double upperLatitude;
    private final Double lowerLatitude;
    private final Double upperLongitude;
    private final Double lowerLongitude;
    private final Double centerLatitude;
    private final Double centerLongitude;
}
