package com.metamong.mt.domain.admin.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metamong.mt.domain.admin.dto.response.ApprovalRequestDto;
import com.metamong.mt.domain.admin.dto.response.FacilityReservationResponseDto;
import com.metamong.mt.domain.admin.dto.response.FacilitySearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.MemberSearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.ReportedMemberResponseDto;
import com.metamong.mt.domain.admin.dto.response.SalesExportDto;
import com.metamong.mt.domain.admin.dto.response.WeekReservationDto;
import com.metamong.mt.domain.admin.service.AdminService;
import com.metamong.mt.global.apispec.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
	
	private final AdminService adminService;

//	private Set<WebSocketSession> sessions = new HashSet<>();
//	
//  public void addSession(WebSocketSession session) {
//  sessions.add(session);

//
//@GetMapping("/members/roleUserCount")
//public String getRoleUserCount() {
//  return memberService.view();
//}
//
//@GetMapping("/download/{filename}")
//public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
//	
//  String fileDirectory = "C:/Users/KOSA/Downloads/";
//  File file = new File(fileDirectory + filename);
//  
//  if (!file.exists()) {
//      return ResponseEntity.notFound().build();  // 파일이 없을 때 처리해놓깅
//  }
//
//  Resource resource = new FileSystemResource(file);
//
//  return ResponseEntity.ok()
//          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")  
//          .contentType(MediaType.parseMediaType("application/octet-stream"))  
//          .body(resource);
//}
//
//	public void removeSession(WebSocketSession session) {
//        sessions.remove(session);
//    }
//
//    public Set<WebSocketSession> getAllSessions() {
//        return sessions;
//    }
	
	
	@GetMapping("/searchMembers")
	public ResponseEntity<BaseResponse<List<MemberSearchResponseDto>>> searchMembers() {
	    return ResponseEntity.ok(BaseResponse.of(adminService.searchMembers(), HttpStatus.OK));
	}


    @GetMapping("/reportedMembers")
    public ResponseEntity<BaseResponse<List<ReportedMemberResponseDto>>> getReportedMembers() {
        return ResponseEntity.ok(BaseResponse.of(adminService.getReportedMembers(), HttpStatus.OK));
    }

    @PostMapping("/banMembers")
    public ResponseEntity<String> processReportBans(@RequestBody List<Long> reportedIds) {
        try {
        	adminService.processReportBans(reportedIds);

            return ResponseEntity.ok("정지 작업이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("정지 작업 중 오류가 발생했습니다.");
        }
    }



	@GetMapping("/getRequestFacilities")
	public ResponseEntity<BaseResponse<List<ApprovalRequestDto>>> getRequestFacilities() {
	    return ResponseEntity.ok(BaseResponse.of(adminService.getRequestFacilities(), HttpStatus.OK));
	}
	
    @PostMapping("/facility/{fctId}/approval")
    public ResponseEntity<String> approveFacilityRegisterRequest(@PathVariable Long fctId) {
        try {
        	adminService.approveFacilityRegisterRequest(fctId);
            return ResponseEntity.ok("등록 요청이 승인되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("승인처리가 실패되었습니다.");
        }
    }

    @PostMapping("/facility/{fctId}/rejection")
    public ResponseEntity<String> rejectFacilityRegisterRequest(@PathVariable Long fctId) {
        try {
        	// 반려 상
        	adminService.rejectFacilityRegisterRequest(fctId);
            return ResponseEntity.ok("등록 요청이 반려되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("반려처리가 실패되었습니다.");
        }
    }


    @PostMapping("/facility/{fctId}/deletion/approval")
    public ResponseEntity<String> approveFacilityDeleteRequest(@PathVariable Long fctId) {
        try {
        	adminService.approveFacilityDeleteRequest(fctId);
            return ResponseEntity.ok("삭제 요청이 승인되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("승인처리가 실패되었습니다.");
        }
    }

    @PostMapping("/facility/{fctId}/deletion/rejection")
    public ResponseEntity<String> rejectFacilityDeleteRequest(@PathVariable Long fctId) {
        try {
        	adminService.rejectFacilityDeleteRequest(fctId);
            return ResponseEntity.ok("삭제 요청이 반려되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("반려처리가 실패되었습니다.");
        }
    }

	@GetMapping("/searchFacilities")
	public ResponseEntity<BaseResponse<List<FacilitySearchResponseDto>>> searchFacilities() {
	    // 공유존인지 컬럼있어야함
		return ResponseEntity.ok(BaseResponse.of(adminService.searchFacilities(), HttpStatus.OK));
	}
	
	// 결제 내역 조회
    @GetMapping("/salesExport")
    public ResponseEntity<List<SalesExportDto>> getPaymentDetails() {
        List<SalesExportDto> payments = adminService.getPaymentDetails();
        return ResponseEntity.ok(payments);
    }

    // 총금액 조회
    @GetMapping("/totalAmt")
    public ResponseEntity<BigDecimal> getTotalRevenue() {
        BigDecimal totalRevenue = adminService.getTotalRevenue();
        return ResponseEntity.ok(totalRevenue);
    }
    
    // 대시보드
    // 요일별 / 시간대별 주간 예약현황(히트맵) 
    // 각 시설 별 실시간 예약 건수 및 매출 및 취소율 (복합 막대 그래프)
    // 월별 예약 top 5 시설(막대 or 원형 or 파이)
    // 월별 매출 top 5 시설(막대 or 누적 영역 차트)
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getReservationsStats() {
        Map<String, Object> stats = new HashMap<>();

        // 주간 예약 데이터 가져오기
        List<WeekReservationDto> weekReservations = adminService.getThisWeekReservations();
        stats.put("weekReservations", weekReservations);

        // 시설별 예약 통계 가져오기
        List<FacilityReservationResponseDto> totalReservations = adminService.getTotalReservations();
        List<FacilityReservationResponseDto> cancelledReservations = adminService.getCancelledReservations();
        List<FacilityReservationResponseDto> totalRevenue = adminService.getTotalByFacility();

        stats.put("totalReservations", totalReservations);
        stats.put("cancelledReservations", cancelledReservations);
        stats.put("totalRevenue", totalRevenue);

        return ResponseEntity.ok(stats);
    }

    
}
