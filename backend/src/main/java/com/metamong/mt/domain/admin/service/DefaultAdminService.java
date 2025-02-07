package com.metamong.mt.domain.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.admin.dto.response.MemberSearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.ReportedMemberResponseDto;
import com.metamong.mt.domain.admin.repository.mybatis.AdminMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultAdminService implements AdminService{
	
	private final AdminMapper adminMapper;
	
//	@Override
//	@Transactional(readOnly = true)
//	public List<MemberSearchResponseDto> searchMembers(MemberSearchRequestDto requestDto) {
//		return adminMapper.searchMembers(requestDto);
//	}
	
	@Override
	@Transactional(readOnly = true)
	public List<MemberSearchResponseDto> searchMembers() {
		return adminMapper.searchMembers();
	}

	@Override
	public List<ReportedMemberResponseDto> getReportedMembers() {
		return adminMapper.getReportedMembers();
	}


	

}
