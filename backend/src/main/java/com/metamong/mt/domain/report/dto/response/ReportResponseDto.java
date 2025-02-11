package com.metamong.mt.domain.report.dto.response;

import java.time.LocalDateTime;

import com.metamong.mt.domain.report.model.constant.ReportType;
import com.metamong.mt.global.constant.BooleanAlt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {
    private Long reportId;
    private Long reporterId;
    private Long reportedId;
    private String reportMsg;
    private LocalDateTime reportDate;
    private ReportType reportType;
    private BooleanAlt isProcessed;
}
