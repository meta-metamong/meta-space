package com.metamong.mt.domain.report.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReportProcessRequestDto {
    @NotEmpty(message="신고 아이디는 필수입니다.")
    private Long reportId;
    
    @NotEmpty(message="정지 일수는 필수입니다")
    private Integer period;
}
