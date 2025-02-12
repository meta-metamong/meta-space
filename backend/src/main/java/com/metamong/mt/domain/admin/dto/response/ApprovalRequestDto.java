package com.metamong.mt.domain.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalRequestDto {
	private String fctId;
    private String fctName;
	private String catName;
	int unitUsageTime;
	private String fullAddress;
	private String fctTel;
	private String isOpenOnHolidays;
    private String zoneName;
    private String fctState;
}
