package com.metamong.mt.domain.admin.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.admin.dto.response.ApprovalRequestDto;
import com.metamong.mt.domain.admin.dto.response.DashBoardDto;
import com.metamong.mt.domain.admin.dto.response.FacilitySearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.MemberSearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.ReportedMemberResponseDto;
import com.metamong.mt.domain.admin.dto.response.SalesExportDto;
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

    @Override
    @Transactional(readOnly = true)
    public List<MemberSearchResponseDto> searchMembers() {
        // 알림 생성 서비스 호출
        notificationService.createNotification(1L, "앙림");

        // 추가적으로 웹소켓을 통한 알림 전달 구현 (필요시)
        //notificationService.sendNotificationToWebSocket(receiverId, notificationMessage);
        return adminMapper.searchMembers();
    }

    @Override
    public List<ReportedMemberResponseDto> getReportedMembers() {
        return adminMapper.getReportedMembers();
    }

    @Override
    @Transactional
    public void processReportBans(List<Long> reportedIds) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            // 신고 횟수 조회
            List<Map<String, Object>> reportCounts = sqlSession.selectList("com.metamong.mt.domain.admin.repository.mybatis.AdminMapper.getReportCounts",reportedIds);


            List<Map<String, Object>> updateDataList = new ArrayList<>();
            List<Integer> deleteDataList = new ArrayList<>();

            for (Map<String, Object> reportData : reportCounts) {
            	int reportedId = ((BigDecimal) reportData.get("REPORTEDID")).intValue();
            	int reportCount = ((BigDecimal) reportData.get("REPORTCOUNT")).intValue();


                updateDataList.add(Map.of("reportedId", reportedId, "reportCount", reportCount));

                // 신고 횟수가 3 이상이면 영구 정지 후 신고 데이터 삭제
                if (reportCount >= 3) {
                    deleteDataList.add(reportedId);
                }
            }


            if (!updateDataList.isEmpty()) {
                sqlSession.update("com.metamong.mt.domain.admin.repository.mybatis.AdminMapper.updateMemberBan", updateDataList);
            }
            if (!deleteDataList.isEmpty()) {
                sqlSession.delete("com.metamong.mt.domain.admin.repository.mybatis.AdminMapper.deleteReportedData", deleteDataList);
            }

            sqlSession.commit();  // 트랜잭션 커밋
        }


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
        Map<String, Object> notificationParams = new HashMap<>();
        notificationParams.put("receiverId", fctId);
        notificationParams.put("notiMsg", "삭제요청이 반려되었습니다");
        adminMapper.insertNotification(notificationParams);
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
	public List<DashBoardDto> getThisWeekReservations() {
		return adminMapper.getReservationsThisWeek();
	}
	

}
