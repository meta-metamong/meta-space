package com.metamong.mt.domain.admin.repository.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.admin.dto.response.MemberSearchResponseDto;
import com.metamong.mt.domain.admin.dto.response.ReportedMemberResponseDto;

@Repository
@Mapper
public interface AdminMapper {
	List<MemberSearchResponseDto> searchMembers();
	
	List<ReportedMemberResponseDto> getReportedMembers();
	
    void updateFacilityStateRegApproved(Map<String, Object> params);

    void insertNotification(Map<String, Object> params);
    
    void updateFacilityStateRegRejected(Map<String, Object> params);
    
    void updateFacilityStateDelApproved(Map<String, Object> params);
    
    void updateFacilityStateDelRejected(Map<String, Object> params);
}
