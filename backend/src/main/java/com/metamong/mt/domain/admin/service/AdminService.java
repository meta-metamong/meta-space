package com.metamong.mt.domain.admin.service;

import java.util.List;

import com.metamong.mt.domain.admin.request.MemberSearchRequestDto;
import com.metamong.mt.domain.admin.response.MemberSearchResponseDto;

public interface AdminService {
	List<MemberSearchResponseDto> searchMembers(MemberSearchRequestDto requestDto);
}
