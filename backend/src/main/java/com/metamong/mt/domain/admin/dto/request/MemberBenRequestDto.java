package com.metamong.mt.domain.admin.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberBenRequestDto {
	private Long reportedId;  // 신고된 회원 ID
    private Integer reportCount;  // 신고 횟수
    
    @Override
    public String toString() {
        return "MemberBenRequestDto{" +
                "reportedId=" + reportedId +
                ", reportCount=" + reportCount +
                '}';
    }
}
