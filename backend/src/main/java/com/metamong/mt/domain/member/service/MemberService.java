package com.metamong.mt.domain.member.service;

import com.metamong.mt.domain.member.dto.request.ConsumerSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.LoginRequestDto;
import com.metamong.mt.domain.member.dto.request.ProviderSignUpRequestDto;
import com.metamong.mt.domain.member.dto.response.MemberResponseDto;
import com.metamong.mt.domain.member.model.Member;

public interface MemberService {

	// 로그인
	Long login(LoginRequestDto dto);
	
	// 일반 회원 정보 저장
    void saveConsumer(ConsumerSignUpRequestDto dto);

    // 업주 회원 정보 저장
    void saveProvider(ProviderSignUpRequestDto dto);

    
    // 리프레시 토큰 업데이트
    void updateRefreshToken(Long memberId, String refreshToken);
    
    // 리프레시 토큰 삭제
    void deleteRefreshToken(Long memberId);
    
    // 회원 정보 조회
 	MemberResponseDto searchMember(Long memberId);

    // DB에서 회원 데이터 조회	
    <T> Member getMember(Long memberId);
    
    /*
    
    
    // Refresh Token을 DB에 저장하는 메서드 추가
    
    // void storeRefreshToken(Member member);
    
    // 회원 전체 조회
	
	void updateMember(String userId, UpdateRequestDto dto);
	
	void sendLoginInfoNotificationMail(FindMemberRequestDto request);
	
	void getRoleUserCount();
	
	String view();
	
	// 회원 중복 여부 조회
	boolean isDuplicatedIdOrEmail(String data, String type);
	*/
}
