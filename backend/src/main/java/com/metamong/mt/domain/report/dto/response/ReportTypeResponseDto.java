package com.metamong.mt.domain.report.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportTypeResponseDto {
    private String reportType;
    private String description;
}
