package com.metamong.mt.member.service;

import com.metamong.mt.member.dto.request.FindMemberRequestDto;
import com.metamong.mt.member.dto.request.LoginRequestDto;
import com.metamong.mt.member.dto.request.OwnerSignUpRequestDto;
import com.metamong.mt.member.dto.request.UserSignUpRequestDto;
import com.metamong.mt.member.dto.response.LoginInfoResponseDto;
import com.metamong.mt.member.model.Member;

public interface MemberService {
    void saveUser(UserSignUpRequestDto dto);
    
    void saveOwner(OwnerSignUpRequestDto dto);
    
    void updateRefreshToken(String userId, String refreshToken);
    
    // Refresh Token을 DB에 저장하는 메서드 추가
//    void storeRefreshToken(Member member);
    
    LoginInfoResponseDto findLoginInfo(LoginRequestDto dto);
    
	Member findMember(String userId);
	
	void deleteRefreshToken(String userId);
	
	boolean sendLoginInfoNotificationMail(FindMemberRequestDto request);
}
