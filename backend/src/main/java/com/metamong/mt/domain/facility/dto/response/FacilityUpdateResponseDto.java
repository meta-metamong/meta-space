package com.metamong.mt.domain.facility.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class FacilityUpdateResponseDto {
    private final List<Long> generatedZoneIds;
    private final List<Long> generatedAdditionalInfoIds;
}
