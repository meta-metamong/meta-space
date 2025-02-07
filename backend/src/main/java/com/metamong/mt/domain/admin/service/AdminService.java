package com.metamong.mt.domain.admin.service;

import java.util.List;

import com.metamong.mt.domain.admin.dto.request.MemberSearchRequestDto;
import com.metamong.mt.domain.admin.dto.response.MemberSearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.ReportedMemberResponseDto;

public interface AdminService {
	List<MemberSearchResponseDto> searchMembers();
	List<ReportedMemberResponseDto> getReportedMembers();

}
