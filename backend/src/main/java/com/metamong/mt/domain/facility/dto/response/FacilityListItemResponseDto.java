package com.metamong.mt.domain.facility.dto.response;

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
public class FacilityListItemResponseDto {
    private Long fctId;
    private String fctName;
    private Long catId;
    private String catName;
    private String repImgUrl;
    private double fctLatitude;
    private double fctLongitude;
    private String fctAddress;
    private String fctDetailAddress;
}
