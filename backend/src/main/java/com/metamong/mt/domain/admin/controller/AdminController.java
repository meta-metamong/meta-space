package com.metamong.mt.domain.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.metamong.mt.domain.admin.dto.response.MemberSearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.ReportedMemberResponseDto;
import com.metamong.mt.domain.admin.service.AdminService;
import com.metamong.mt.global.apispec.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
	
	private final AdminService adminService;
	
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
    
    @PostMapping("/approve")
    public ResponseEntity<String> approveFacilityRequest(
            @RequestParam Long provId) {
        try {
        	adminService.approveFacilityRequest(provId);
            return ResponseEntity.ok("등록 요청이 승인되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("승인이 실패되었습니다.");
        }
    }
    
}
