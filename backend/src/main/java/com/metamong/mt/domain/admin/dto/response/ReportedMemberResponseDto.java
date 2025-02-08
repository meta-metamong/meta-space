package com.metamong.mt.domain.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportedMemberResponseDto {
	private String email;
    private String reportType;
    private String reportMsg;
}
