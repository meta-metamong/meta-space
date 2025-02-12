package com.metamong.mt.domain.admin.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import com.metamong.mt.domain.admin.dto.request.MemberSearchRequestDto;
import com.metamong.mt.domain.admin.dto.response.ApprovalRequestDto;
import com.metamong.mt.domain.admin.dto.response.FacilityReservationResponseDto;
import com.metamong.mt.domain.admin.dto.response.FacilitySearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.MemberSearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.RankPaymentDto;
import com.metamong.mt.domain.admin.dto.response.RankReservationDto;
import com.metamong.mt.domain.admin.dto.response.RefundMemberResponseDto;
import com.metamong.mt.domain.admin.dto.response.ReportDetailResponseDto;
import com.metamong.mt.domain.admin.dto.response.ReportedMemberResponseDto;
import com.metamong.mt.domain.admin.dto.response.SalesExportDto;
import com.metamong.mt.domain.admin.dto.response.WeekReservationDto;
import com.metamong.mt.domain.admin.repository.mybatis.AdminMapper;
import com.metamong.mt.domain.notification.model.Notification;
import com.metamong.mt.domain.notification.repository.jpa.NotificationRepository;
import com.metamong.mt.domain.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultAdminService implements AdminService{
	
	private final AdminMapper adminMapper;
    private final SqlSessionFactory sqlSessionFactory;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final Set<WebSocketSession> sessions = new HashSet<>();
    private int roleUserCount; 
    private Date lastExecutionTime;
    
    @Override
    @Transactional(readOnly = true)
    public List<MemberSearchResponseDto> searchMembers() {

        // 추가적으로 웹소켓을 통한 알림 전달 구현 (필요시)
        //notificationService.sendNotificationToWebSocket(receiverId, notificationMessage);

        return adminMapper.searchMembers();
    }

    @Override
    public List<ReportedMemberResponseDto> getReportedMembers() {
        return adminMapper.getReportedMembers();
    }

    public List<ReportDetailResponseDto> getReportDetails(Long memId) {
        return adminMapper.selectReportDetails(memId);
    }
    @Override
    @Transactional
    public void processReportBansBatch(List<Long> reportedIds) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            // 신고 횟수 조회
            List<Map<String, Object>> reportCounts = sqlSession.selectList(
                "com.metamong.mt.domain.admin.repository.mybatis.AdminMapper.getReportCounts", 
                reportedIds
            );

            List<Map<String, Object>> updateDataList = new ArrayList<>();
            List<Long> deleteDataList = new ArrayList<>();

            for (Map<String, Object> reportData : reportCounts) {
                long reportedId = reportData.get("REPORTEDID") != null ? ((BigDecimal) reportData.get("REPORTEDID")).longValue() : 0;
                int reportCount = reportData.get("REPORTCOUNT") != null ? ((BigDecimal) reportData.get("REPORTCOUNT")).intValue() : 0;

                // Map으로 변환하여 MyBatis가 인식할 수 있게 처리
                Map<String, Object> data = new HashMap<>();
                data.put("reportedId", reportedId);
                data.put("reportCount", reportCount);
                updateDataList.add(data);

                // 신고 횟수가 3 이상이면 영구 정지 후 신고 데이터 삭제
                if (reportCount >= 3) {
                    deleteDataList.add(reportedId);
                }
            }

            // updateDataList를 MyBatis가 인식할 수 있도록 Map으로 변환
            if (!updateDataList.isEmpty()) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("list", updateDataList); // "list"라는 키로 전달

                sqlSession.update("com.metamong.mt.domain.admin.repository.mybatis.AdminMapper.updateMemberBanBatch", paramMap);
            }

            // delete 쿼리 실행
            if (!deleteDataList.isEmpty()) {
                sqlSession.delete("com.metamong.mt.domain.admin.repository.mybatis.AdminMapper.deleteReportedData", deleteDataList);
            }

            sqlSession.commit();  // 트랜잭션 커밋
        }
    }

    @Override
    public List<RefundMemberResponseDto> getRefundMembers() {
        return adminMapper.getRefundMembers();
    }

	@Override
	@Transactional
	public void approveFacilityRegisterRequest(Long fctId) {
		// 1. 요청 승인 (fct_state 업데이트)
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("fctId", fctId);
        adminMapper.updateFacilityStateRegApproved(updateParams);

        // 2. 알림 테이블에 알림 삽입
        Notification notification = new Notification();
        notification.setReceiverId(fctId);
        notification.setNotiMsg("등록 요청이 승인되었습니다.");
        notification.setIsRead('N');  // 기본값 'N' 설정
        notificationRepository.save(notification);
        // Notification 저장 (JPA 사용)
       
	}

	@Override
	@Transactional
	public void rejectFacilityRegisterRequest(Long fctId) {
		// 1. 요청 승인 (fct_state 업데이트)
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("fctId", fctId);
        adminMapper.updateFacilityStateRegRejected(updateParams);

        // 2. 알림 테이블에 알림 삽입
        Map<String, Object> notificationParams = new HashMap<>();
        notificationParams.put("receiverId", fctId);
        notificationParams.put("notiMsg", "등록요청이 반려되었습니다");
        adminMapper.insertNotification(notificationParams);
	}

	@Override
	@Transactional
	public void approveFacilityDeleteRequest(Long fctId) {
		// 1. 요청 승인 (fct_state 업데이트)
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("fctId", fctId);
        adminMapper.updateFacilityStateDelApproved(updateParams);

        // 2. 알림 테이블에 알림 삽입
        Map<String, Object> notificationParams = new HashMap<>();
        notificationParams.put("receiverId", fctId);
        notificationParams.put("notiMsg", "삭제요청이 승인되었습니다");
        adminMapper.insertNotification(notificationParams);

	}

	@Override
	@Transactional
	public void rejectFacilityDeleteRequest(Long fctId) {
		// 1. 요청 승인 (fct_state 업데이트)
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("fctId", fctId);
        adminMapper.updateFacilityStateDelRejected(updateParams);

        // 2. 알림 테이블에 알림 삽입
        Notification notification = new Notification();
        notification.setReceiverId(fctId);
        notification.setNotiMsg("삭제요청이 반려되었습니다");
        notification.setIsRead('N');  // 기본적으로 'N' (읽지 않음)으로 설정
        notificationRepository.save(notification);  // JPA를 통해 저장
	}

	@Override
	public List<FacilitySearchResponseDto> searchFacilities() {
		return adminMapper.searchFacilities();
	}

	@Override
	public List<ApprovalRequestDto> getRequestFacilities() {
		return adminMapper.getRequestFacilities();
	}

	@Override
	public List<SalesExportDto> getPaymentDetails() {
		return adminMapper.getPaymentDetails();
	}

	@Override
	public BigDecimal getTotalRevenue() {
		return adminMapper.getTotalRevenue();
	}

	@Override
	public List<WeekReservationDto> getThisWeekReservations() {
		return adminMapper.getReservationsThisWeek();
	}
	
    public List<FacilityReservationResponseDto> getTotalReservations() {
        return adminMapper.getTotalReservations();
    }

    public List<FacilityReservationResponseDto> getCancelledReservations() {
        return adminMapper.getCancelledReservations();
    }	

    public List<FacilityReservationResponseDto> getTotalByFacility() {
        return adminMapper.getTotalByFacility();
    }
    
    public List<RankReservationDto> getRankReservation() {
        return adminMapper.getRankReservation();
    }
    
    public List<RankPaymentDto> getRankPayment() {
        return adminMapper.getRankPayment();
    }
    
    public List<WeekReservationDto> getReservationsByHourThisWeek() {
        return adminMapper.getReservationsByHourThisWeek();
    }


    public void updateMemberBan(List<Map<String, Integer>> reportData) {
        Map<String, Object> params = new HashMap<>();
        params.put("reportData", reportData);
        
        adminMapper.updateMemberBan(params);
    }
    
    public List<Long> getMembersToUnban() {
    	 return adminMapper.getMembersToUnban();
    }
    public void unbanMembers(List<Long> memberIds) {
    	 if (memberIds != null && !memberIds.isEmpty()) {
             adminMapper.unbanMembers(memberIds); // MyBatis에서 처리할 쿼리 실행
         }
    }

	@Scheduled(cron = "0 0/5 * * * ?")
    public void getRoleUserCount() {
        roleUserCount = adminMapper.countRoleUserMembers();
        lastExecutionTime = new Date(); 
    }

    public String view() {
    	return "개수"+roleUserCount;
    }
    
	// 정지 해제할 회원들의 ID 목록을 가져오기
    public void unbanMembers() {
    List<Long> membersToUnban = adminMapper.getMembersToUnban();
    
    if (membersToUnban != null && !membersToUnban.isEmpty()) {
    	adminMapper.unbanMembers(membersToUnban);
        System.out.println("정지 해제된 회원들: " + membersToUnban);
    } else {
    	System.out.println("정지 해제 대상 회원이 없습니다.");
    }
    
}

	@Override
	public List<WeekReservationDto> getRedisReservationsThisWeek() {
		return adminMapper.getRedisReservationsThisWeek();
	}


}
