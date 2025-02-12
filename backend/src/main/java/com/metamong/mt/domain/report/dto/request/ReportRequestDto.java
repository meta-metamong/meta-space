package com.metamong.mt.domain.report.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReportRequestDto {
    @NotEmpty(message="존 아이디는 필수입니다.")
    private Long zoneId;
    
    @NotEmpty(message="신고 메세지는 필수입니다")
    private String reportMsg;
    
    @NotEmpty(message="신고 유형은 필수입니다.")
    private String reportType;
}
