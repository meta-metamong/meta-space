package com.metamong.mt.member.service;

import com.metamong.mt.member.dto.request.FindMemberRequestDto;
import com.metamong.mt.member.dto.response.LoginResponseDto;
import com.metamong.mt.member.model.Member;

public interface IMemberService {
    void insertMember(Member member);
    // Refresh Token을 DB에 저장하는 메서드 추가
    void storeRefreshToken(Member member);
    LoginResponseDto selectLoginMember(String userid);
	Member selectMemberEntity(String userId);
	
	boolean findMember(FindMemberRequestDto request);
}
