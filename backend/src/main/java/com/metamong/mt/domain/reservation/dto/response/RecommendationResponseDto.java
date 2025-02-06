package com.metamong.mt.domain.reservation.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendationResponseDto {
	@JsonProperty("mem_id")
    private Long memberId;
	
	@JsonProperty("recommended_facilities")
    private List<Integer> recommendedFacilities;
}
