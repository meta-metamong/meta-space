package com.metamong.mt.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.member.dto.request.FindMemberRequestDto;
import com.metamong.mt.member.dto.response.LoginResponseDto;
import com.metamong.mt.member.exception.MemberNotFoundException;
import com.metamong.mt.member.model.Member;
import com.metamong.mt.member.model.Role;
import com.metamong.mt.member.repository.jpa.MemberRepository;
import com.metamong.mt.member.repository.mybatis.MemberMapper;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class DefaultMemberService implements MemberService {
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;
    
    
    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto selectLoginMember(String userid) {
        return memberMapper.selectMember(userid);
    }
    
    @Override
    @Transactional
    public void insertMember(Member member) {
    	if (member.getRole() == Role.ROLE_OWNER) {
            if (member.getBusinessName() == null || member.getBusinessRegistrationNumber() == null) {
                throw new IllegalArgumentException("업주 회원가입 시 사업체명과 사업자등록번호는 필수입니다.");
            }
        }
    	this.memberRepository.save(member);
    }

 

	@Override
	@Transactional(readOnly = true)
	public Member selectMemberEntity(String userId) {
	    return this.memberRepository.findById(userId)
	            .orElseThrow(() -> new MemberNotFoundException(userId, "회원을 찾을 수 없습니다."));
	}

	@Override
	@Transactional 
	public void storeRefreshToken(Member member) {

        // 사용자가 존재하는 경우, refreshToken을 저장
        if (member != null) {
        	member.setRefreshToken(member.getRefreshToken());  
            memberMapper.updateMember(member);  
        } else {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
		
	}
	
	@Override
    @Transactional
    public void removeRefreshToken(String userId) {
	    Member member = selectMemberEntity(userId);
	    member.setRefreshToken(null);
    }
	
	@Override
	public boolean findMember(FindMemberRequestDto request) {	
		boolean requestSuccess = true;
		
		/*
		 * idOrPw가 id인 경우 name, email 데이터만 오고, userid는 null값으로 온다.
		 * idOrPw가 pw인 경우 userid, name, email 데이터 전부 온다.
		 */
		System.out.println("====================================");
		System.out.println("idOrPw: " + request.getIdOrPw());
		System.out.println("userid: " + request.getUserid());
		System.out.println("name: " + request.getName());
		System.out.println("email: " + request.getEmail());
		// 이메일 인증 요청 성공 시
		if(1 == 1){
			requestSuccess = true;	
		}
		// 이메일 인증 요청 실패 시
		else {
			requestSuccess = false;
		}
		
		return requestSuccess;
	}
    
}
