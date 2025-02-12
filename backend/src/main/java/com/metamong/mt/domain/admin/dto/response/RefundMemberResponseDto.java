package com.metamong.mt.domain.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefundMemberResponseDto {
	private Long rvtId;
	private String memName;
	private int payPrice;
	private String rvtCancelationReason;
	private String refundInfo;
}
