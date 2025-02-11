package com.metamong.mt.domain.report.service;

import java.util.List;

import com.metamong.mt.domain.report.dto.request.ReportProcessRequestDto;
import com.metamong.mt.domain.report.dto.request.ReportRequestDto;
import com.metamong.mt.domain.report.dto.response.ReportResponseDto;
import com.metamong.mt.domain.report.dto.response.ReportTypeResponseDto;
import com.metamong.mt.domain.report.model.Report;

public interface ReportService {
    /**
     * 신고 접수 메서드입니다.
     * @param ReportRequestDto, memId 존 ID, 신고 내용, 신고 날짜, 신고 유형, 신고자 아이디
     */
    void report(ReportRequestDto dto, Long memId);
    
    /**
     * 신고 목록 조회 메서드
     * @return List<ReportResponseDto> 신고 목록
     */
    List<ReportResponseDto> getReports();
    
    /**
     * 신고 처리 메서드
     * 입력한 일수만큼 정지 기한을 설정합니다.
     * 0을 입력하면 영구 정지라 판단하여 약 273,972년 후에 정지 상태가 해제됩니다.
     * @param Long reportId
     * @param Integer period
     */
    void processReport(ReportProcessRequestDto dto);
    
    /**
     * 신고 조회 메서드
     * @param Long reportId
     * @return Report
     */
    Report getReportByRepository(Long reportId);
    
    /**
     * 모든 신고 유형을 조회하는 메서드
     * @return List<ReportTypeResponseDto>
     */
    List<ReportTypeResponseDto> getAllReportTypes();
}
