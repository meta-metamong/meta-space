package com.metamong.mt.domain.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IsReportableRequestDto {   
    @NotNull(message="존 아이디는 필수입니다.")
    private Long zoneId;
}
