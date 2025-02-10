package com.metamong.mt.domain.member.service;

import java.util.List;

import com.metamong.mt.domain.facility.dto.response.ImageUploadUrlResponseDto;
import com.metamong.mt.domain.member.dto.request.ConsumerSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.LoginRequestDto;
import com.metamong.mt.domain.member.dto.request.PasswordChangeRequestDto;
import com.metamong.mt.domain.member.dto.request.PasswordConfirmRequestDto;
import com.metamong.mt.domain.member.dto.request.ProviderSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.UpdateRequestDto;
import com.metamong.mt.domain.member.dto.response.BankResponseDto;
import com.metamong.mt.domain.member.dto.response.MemberResponseDto;
import com.metamong.mt.domain.member.model.Account;
import com.metamong.mt.domain.member.model.FctProvider;
import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.MemberImage;

public interface MemberService {

	// 로그인
	Long login(LoginRequestDto dto);
	
	boolean isValidPassword(Long memId, String password);
	
	// 일반 회원 정보 저장
    void saveConsumer(ConsumerSignUpRequestDto dto);

    // 업주 회원 정보 저장
    void saveProvider(ProviderSignUpRequestDto dto);
    
    // 리프레시 토큰 업데이트
    void updateRefreshToken(Long memId, String refreshToken);
    
    // 리프레시 토큰 삭제
    void deleteRefreshToken(Long memId);
    
    // 회원 정보 조회
 	MemberResponseDto searchMember(Long memId);

 	// 회원 정보 수정
 	ImageUploadUrlResponseDto updateMember(Long memId, UpdateRequestDto dto);
 	
 	// 회원 정보 삭제
	boolean deleteMember(Long memId);

 	// 비밀번호 확인
	void confirmPassword(Long memId, PasswordConfirmRequestDto dto);
	
 	// 비밀번호 변경
 	void changePassword(Long memId, PasswordChangeRequestDto dto);

 	// 매퍼를 통해 DB에서 회원 데이터 조회 (탈퇴하지 않은 회원)
    Member getMemberByMapper(Long memId);
    
    // Jpa를 통해 DB에서 회원 데이터 조회 (삭제 여부 상관 x)
    Member getMemberByRepository(Long memId);
    
    // DB에서 시설제공자 데이터 조회
    FctProvider getProvider(Long memId);
    
    // DB에서 시설제공자의 계좌 데이터 조회
    Account getAccount(Long memId);
    
    // DB에서 모든 은행 정보 데이터 조회
    List<BankResponseDto> getAllBanks();
}
