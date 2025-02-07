package com.metamong.mt.domain.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	@GetMapping("/search-members")
	public ResponseEntity<BaseResponse<List<MemberSearchResponseDto>>> searchMembers() {
	    return ResponseEntity.ok(BaseResponse.of(adminService.searchMembers(), HttpStatus.OK));
	}

    
    @GetMapping("/reportedMembers")
    public ResponseEntity<BaseResponse<List<ReportedMemberResponseDto>>> getReportedMembers() {
        return ResponseEntity.ok(BaseResponse.of(adminService.getReportedMembers(), HttpStatus.OK));
    }
    
    
}
