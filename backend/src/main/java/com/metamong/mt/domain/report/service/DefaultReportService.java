package com.metamong.mt.domain.report.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.facility.service.FacilityService;
import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.service.MemberService;
import com.metamong.mt.domain.report.dto.request.ReportProcessRequestDto;
import com.metamong.mt.domain.report.dto.request.ReportRequestDto;
import com.metamong.mt.domain.report.dto.response.ReportResponseDto;
import com.metamong.mt.domain.report.dto.response.ReportTypeResponseDto;
import com.metamong.mt.domain.report.exception.ReportNotFoundException;
import com.metamong.mt.domain.report.model.Report;
import com.metamong.mt.domain.report.model.constant.ReportType;
import com.metamong.mt.domain.report.repository.jpa.ReportRepository;
import com.metamong.mt.global.constant.BooleanAlt;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultReportService implements ReportService{
    private final ReportRepository reportRepository;
    private final FacilityService facilityService;
    private final MemberService memberService;
    
    @Override
    @Transactional
    public void report(ReportRequestDto dto, Long memId) {
        Long reportedId = facilityService.getMemberIdByZoneId(dto.getZoneId());
        reportRepository.save(Report.builder()
                                    .reporterId(memId)
                                    .reportedId(reportedId)
                                    .reportMsg(dto.getReportMsg())
                                    .reportDate(LocalDateTime.now())
                                    .reportType(ReportType.valueOf(dto.getReportType()))
                                    .isProcessed(BooleanAlt.N)
                                    .build());
    }

    @Override
    @Transactional(readOnly=true)
    public List<ReportResponseDto> getReports() {
        return reportRepository.findAll().stream().map(report -> ReportResponseDto.builder()
                                                                                  .reportId(report.getReportId())
                                                                                  .reporterId(report.getReporterId())
                                                                                  .reportedId(report.getReportedId())
                                                                                  .reportMsg(report.getReportMsg())
                                                                                  .reportDate(report.getReportDate())
                                                                                  .reportType(report.getReportType())
                                                                                  .isProcessed(report.getIsProcessed())
                                                                                  .build()).toList();
    }

    @Override
    @Transactional
    public void processReport(ReportProcessRequestDto dto) {
        Report report = this.getReportByRepository(dto.getReportId());
        Member member = memberService.getMemberByRepository(report.getReportedId());
        int banPeriod = dto.getPeriod();
        if(banPeriod == 0) banPeriod = 99999999;
        member.setMemBannedUntil(LocalDateTime.now().plusDays(banPeriod));
        report.process();
    }

    @Override
    public Report getReportByRepository(Long reportId) {
        return reportRepository.findById(reportId).orElseThrow(() -> new ReportNotFoundException("신고 정보를 찾을 수 없습니다."));
    }

    @Override
    public List<ReportTypeResponseDto> getAllReportTypes() {
        return Arrays.stream(ReportType.values())
                .map(type -> new ReportTypeResponseDto(type.name(), type.getDescription())).toList();
    }
    
    
    
    
}
