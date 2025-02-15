package com.metamong.mt.domain.admin.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.metamong.mt.domain.admin.dto.request.MemberSearchRequestDto;
import com.metamong.mt.domain.admin.dto.response.ApprovalRequestDto;
import com.metamong.mt.domain.admin.dto.response.FacilityReservationResponseDto;
import com.metamong.mt.domain.admin.dto.response.FacilitySearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.MemberSearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.MonthlySalesGrowthDTO;
import com.metamong.mt.domain.admin.dto.response.RankPaymentDto;
import com.metamong.mt.domain.admin.dto.response.RankReservationDto;
import com.metamong.mt.domain.admin.dto.response.RefundMemberResponseDto;
import com.metamong.mt.domain.admin.dto.response.ReportDetailResponseDto;
import com.metamong.mt.domain.admin.dto.response.ReportedMemberResponseDto;
import com.metamong.mt.domain.admin.dto.response.SalesExportDto;
import com.metamong.mt.domain.admin.dto.response.WeekReservationDto;

public interface AdminService {
	List<MemberSearchResponseDto> searchMembers();
	List<ReportedMemberResponseDto> getReportedMembers();
	//void processReportBans(List<Long> reportedIds);
	void approveFacilityRegisterRequest(Long fctId);
	void rejectFacilityRegisterRequest(Long fctId);
	void approveFacilityDeleteRequest(Long fctId);
	void rejectFacilityDeleteRequest(Long fctId);
	List<FacilitySearchResponseDto> searchFacilities();
	List<ApprovalRequestDto> getRequestFacilities();
    List<SalesExportDto> getPaymentDetails();
    BigDecimal getTotalRevenue();
    List<WeekReservationDto> getThisWeekReservations();
    List<WeekReservationDto> getRedisReservationsThisWeek();
    List<FacilityReservationResponseDto> getTotalReservations();
    List<FacilityReservationResponseDto> getCancelledReservations();
    List<FacilityReservationResponseDto> getTotalByFacility();
    List<RankReservationDto> getRankReservation();
    List<RankPaymentDto> getRankPayment();
    List<WeekReservationDto> getReservationsByHourThisWeek();
    void processReportBansBatch(List<Long> reportedIds);
    //void updateMemberBan(List<Long> reportedIds, Map<Long, Integer> reportCounts);
    List<ReportDetailResponseDto> getReportDetails(Long memId);
    void updateMemberBan(List<Map<String, Integer>> reportData);
    List<Long> getMembersToUnban();
    void unbanMembers(List<Long> memberIds);
    List<RefundMemberResponseDto> getRefundMembers();
    MonthlySalesGrowthDTO getMonthlySalesGrowth();
    
}
