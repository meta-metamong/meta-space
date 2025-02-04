package com.metamong.mt.domain.member.service;

import com.metamong.mt.domain.member.dto.request.FindMemberRequestDto;
import com.metamong.mt.domain.member.dto.request.LoginRequestDto;
import com.metamong.mt.domain.member.dto.request.OwnerSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.UserSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.UpdateRequestDto;
import com.metamong.mt.domain.member.dto.response.LoginInfoResponseDto;
import com.metamong.mt.domain.member.dto.response.MemberResponseDto;
import com.metamong.mt.domain.member.model.Member;

public interface MemberService {
	// 일반 회원 정보 저장
    void saveUser(UserSignUpRequestDto dto);

    // 업주 회원 정보 저장
    void saveOwner(OwnerSignUpRequestDto dto);

    // 리프레시 토큰 업데이트
    void updateRefreshToken(String userId, String refreshToken);
    
    // 리프레시 토큰 삭제
    void deleteRefreshToken(String userId);
    
    // Refresh Token을 DB에 저장하는 메서드 추가
    // void storeRefreshToken(Member member);
    
    LoginInfoResponseDto findLoginInfo(LoginRequestDto dto);
    
    // 회원 전체 조회
	Member findMember(String userId);
	
	// 회원 정보 조회
	MemberResponseDto getMember(String userId);
	
	void updateMember(String userId, UpdateRequestDto dto);
	
	void sendLoginInfoNotificationMail(FindMemberRequestDto request);
	
	void getRoleUserCount();
	
	String view();
	
	// 회원 중복 여부 조회
	boolean isDuplicatedIdOrEmail(String data, String type);
}
