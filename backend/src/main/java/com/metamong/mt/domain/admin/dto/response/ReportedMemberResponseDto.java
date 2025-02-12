package com.metamong.mt.domain.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportedMemberResponseDto {
	private Long memId;
	private String email;
	private int reportedCnt;
}
