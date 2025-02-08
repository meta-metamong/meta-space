package com.metamong.mt.domain.admin.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FacilitySearchResponseDto {
	String fct_name;
	String zone_name;
	int rvt_cnt;
	int rvt_cancel_cnt;
	Long cons_id;
}
