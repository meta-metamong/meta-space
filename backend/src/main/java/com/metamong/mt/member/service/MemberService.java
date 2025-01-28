package com.metamong.mt.member.service;

import com.metamong.mt.member.dto.request.FindMemberRequestDto;
import com.metamong.mt.member.dto.request.LoginRequestDto;
import com.metamong.mt.member.dto.response.LoginInfoResponseDto;
import com.metamong.mt.member.model.Member;

public interface MemberService {
    void insertMember(Member member);
    
    void updateRefreshToken(String userId, String refreshToken);
    
    // Refresh Token을 DB에 저장하는 메서드 추가
    void storeRefreshToken(Member member);
    LoginInfoResponseDto selectLoginMember(LoginRequestDto dto);
	Member selectMemberEntity(String userId);
	void removeRefreshToken(String userId);
	boolean findMember(FindMemberRequestDto request);
	
	
}
