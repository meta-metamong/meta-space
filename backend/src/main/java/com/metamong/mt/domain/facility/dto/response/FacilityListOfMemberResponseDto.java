package com.metamong.mt.domain.facility.dto.response;

import com.metamong.mt.domain.facility.model.FacilityState;

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
public class FacilityListOfMemberResponseDto {
    private Long fctId;
    private String fctName;
    private String address;
    private FacilityState fctState;
}
