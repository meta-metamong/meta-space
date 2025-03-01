package com.metamong.mt.domain.admin.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailResponseDto {
	private Long memId;
	private String email;
    private String reportType;
    private String reportMsg;
    private LocalDate reportDate;
}
