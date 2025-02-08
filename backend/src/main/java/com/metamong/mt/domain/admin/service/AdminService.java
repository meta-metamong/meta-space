package com.metamong.mt.domain.admin.service;

import java.util.List;

import com.metamong.mt.domain.admin.dto.response.FacilitySearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.MemberSearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.ReportedMemberResponseDto;

public interface AdminService {
	List<MemberSearchResponseDto> searchMembers();
	List<ReportedMemberResponseDto> getReportedMembers();
	void processReportBans(List<Long> reportedIds);
	void approveFacilityRegisterRequest(Long provId);
	void rejectFacilityRegisterRequest(Long provId);
	void approveFacilityDeleteRequest(Long provId);
	void rejectFacilityDeleteRequest(Long provId);
	List<FacilitySearchResponseDto> searchFacilities();
}
