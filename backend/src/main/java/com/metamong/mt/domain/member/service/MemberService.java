package com.metamong.mt.domain.member.service;

import com.metamong.mt.domain.member.dto.request.FindMemberRequestDto;
import com.metamong.mt.domain.member.dto.request.LoginRequestDto;
import com.metamong.mt.domain.member.dto.request.OwnerSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.UserSignUpRequestDto;
import com.metamong.mt.domain.member.dto.response.LoginInfoResponseDto;
import com.metamong.mt.domain.member.dto.response.MyPageInfoResponseDto;
import com.metamong.mt.domain.member.dto.response.MemberResponseDto;
import com.metamong.mt.domain.member.model.Member;

public interface MemberService {
    void saveUser(UserSignUpRequestDto dto);
    
    void saveOwner(OwnerSignUpRequestDto dto);
    
    void updateRefreshToken(String userId, String refreshToken);
    
    // Refresh Token을 DB에 저장하는 메서드 추가
    // void storeRefreshToken(Member member);
    
    LoginInfoResponseDto findLoginInfo(LoginRequestDto dto);
    
	Member findMember(String userId);
	
	MemberResponseDto getMember(String userId);
	
	void deleteRefreshToken(String userId);
	
	void sendLoginInfoNotificationMail(FindMemberRequestDto request);
	
	MyPageInfoResponseDto findMyPageInfo(String userId);
	void registerAnswer();
	

	void getRoleUserCount();
	
	String view();
}
