package com.metamong.mt.domain.admin.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import com.metamong.mt.domain.admin.dto.response.ApprovalRequestDto;
import com.metamong.mt.domain.admin.dto.response.FacilityReservationResponseDto;
import com.metamong.mt.domain.admin.dto.response.WeekReservationDto;
import com.metamong.mt.domain.admin.dto.response.FacilitySearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.MemberSearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.RankReservationDto;
import com.metamong.mt.domain.admin.dto.response.ReportedMemberResponseDto;
import com.metamong.mt.domain.admin.dto.response.SalesExportDto;

public interface AdminService {
	List<MemberSearchResponseDto> searchMembers();
	List<ReportedMemberResponseDto> getReportedMembers();
	void processReportBans(List<Long> reportedIds);
	void approveFacilityRegisterRequest(Long fctId);
	void rejectFacilityRegisterRequest(Long fctId);
	void approveFacilityDeleteRequest(Long fctId);
	void rejectFacilityDeleteRequest(Long fctId);
	List<FacilitySearchResponseDto> searchFacilities();
	List<ApprovalRequestDto> getRequestFacilities();
    List<SalesExportDto> getPaymentDetails();
    BigDecimal getTotalRevenue();
    List<WeekReservationDto> getThisWeekReservations();
    List<FacilityReservationResponseDto> getTotalReservations();
    List<FacilityReservationResponseDto> getCancelledReservations();
    List<FacilityReservationResponseDto> getTotalByFacility();
    List<RankReservationDto> getRankReservation();
    List<WeekReservationDto> getReservationsByHourThisWeek();
}
