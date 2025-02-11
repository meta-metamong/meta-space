package com.metamong.mt.domain.admin.repository.mybatis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.admin.dto.response.ApprovalRequestDto;
import com.metamong.mt.domain.admin.dto.response.FacilityReservationResponseDto;
import com.metamong.mt.domain.admin.dto.response.FacilitySearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.MemberSearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.RankReservationDto;
import com.metamong.mt.domain.admin.dto.response.ReportDetailResponseDto;
import com.metamong.mt.domain.admin.dto.response.ReportedMemberResponseDto;
import com.metamong.mt.domain.admin.dto.response.SalesExportDto;
import com.metamong.mt.domain.admin.dto.response.WeekReservationDto;

@Repository
@Mapper
public interface AdminMapper {
	List<MemberSearchResponseDto> searchMembers();

	List<ReportedMemberResponseDto> getReportedMembers();

    void updateFacilityStateRegApproved(Map<String, Object> params);

    void insertNotification(Map<String, Object> params);

    void updateFacilityStateRegRejected(Map<String, Object> params);

    void updateFacilityStateDelApproved(Map<String, Object> params);

    void updateFacilityStateDelRejected(Map<String, Object> params);

    List<FacilitySearchResponseDto> searchFacilities();
    
    List<ApprovalRequestDto> getRequestFacilities();
    
    // 시설명, zone명, 결제일, 결제금액, 결제방법
    List<SalesExportDto> getPaymentDetails();

    // 총금액
    BigDecimal getTotalRevenue();
    
    List<WeekReservationDto> getReservationsThisWeek();
    
    List<FacilityReservationResponseDto> getTotalReservations();
    
    List<FacilityReservationResponseDto> getCancelledReservations();
    
    List<FacilityReservationResponseDto> getTotalByFacility();
    
    List<RankReservationDto> getRankReservation();
    
    List<WeekReservationDto> getReservationsByHourThisWeek();
    
    //void updateMemberBan(List<Long> reportIds, Map<Long, Integer> reportCounts);
//    void updateMemberBan(@Param("reportIds") List<Long> reportIds, 
//            @Param("reportCounts") Map<Long, Integer> reportCounts);
//    
    List<ReportDetailResponseDto> selectReportDetails(@Param("memId") Long memId);

    //void updateMemberBan(Map<String, Object> params);
    

	void updateMemberBan(Map<String, Object> params);
}
