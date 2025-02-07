package com.metamong.mt.domain.admin.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.admin.dto.response.MemberSearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.ReportedMemberResponseDto;

@Repository
@Mapper
public interface AdminMapper {
	List<MemberSearchResponseDto> searchMembers();
	
	List<ReportedMemberResponseDto> getReportedMembers();
}
