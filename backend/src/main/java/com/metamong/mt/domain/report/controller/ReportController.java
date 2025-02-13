package com.metamong.mt.domain.report.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metamong.mt.domain.report.dto.request.ReportProcessRequestDto;
import com.metamong.mt.domain.report.dto.request.ReportRequestDto;
import com.metamong.mt.domain.report.service.ReportService;
import com.metamong.mt.global.apispec.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    
    @PostMapping
    public ResponseEntity<?> report(@Validated @RequestBody ReportRequestDto dto, @AuthenticationPrincipal User user){
        this.reportService.report(dto, Long.valueOf(user.getUsername()));
        return ResponseEntity.ok(BaseResponse.of(HttpStatus.CREATED, "신고 접수가 완료되었습니다."));
    }
    
    @GetMapping
    public ResponseEntity<?> getAllReport() {
        return ResponseEntity.ok(BaseResponse.of(this.reportService.getReports(), HttpStatus.OK, "모든 신고를 조회했습니다."));
    }
    
    @PutMapping
    public ResponseEntity<?> processReport(@Validated @RequestBody ReportProcessRequestDto dto){
        this.reportService.processReport(dto);
        return ResponseEntity.ok(BaseResponse.of(HttpStatus.OK, "신고 처리가 완료되었습니다."));
    }
    
    @GetMapping("/types")
    public ResponseEntity<?> getReportTypes(){
        return ResponseEntity.ok(BaseResponse.of(this.reportService.getAllReportTypes(), HttpStatus.OK, "모든 신고 유형을 조회했습니다."));
    }
}
