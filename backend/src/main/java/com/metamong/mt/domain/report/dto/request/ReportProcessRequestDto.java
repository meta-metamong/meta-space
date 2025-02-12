package com.metamong.mt.domain.report.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReportProcessRequestDto {
    private Long reportId;
    private Integer period;
}
