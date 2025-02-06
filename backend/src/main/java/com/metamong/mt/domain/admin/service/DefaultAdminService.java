package com.metamong.mt.domain.admin.service;

import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.admin.request.MemberSearchRequestDto;
import com.metamong.mt.domain.member.dto.response.MemberResponseDto;
import com.metamong.mt.domain.member.model.Member;

public class DefaultAdminService {
	
//	@Override
//	@Transactional(readOnly = true)
//	public MemberResponseDto searchMembers(MemberSearchRequestDto requestDto) {
//	    Member member = getMember(requestDto);
//	    return MemberResponseDto.builder()
//								.memId(member.getMemId())
//								.memName(member.getMemName())
//								.email(member.getEmail())
//								.memPhone(member.getMemPhone())
//								.gender(member.getGender())
//								.birthDate(member.getBirthDate())
//								.memPostalCode(member.getMemPostalCode())
//								.memDetailAddress(member.getMemDetailAddress())
//								.memAddress(member.getMemAddress())
//								.role(member.getRole())
//								.build();
//	}
}
