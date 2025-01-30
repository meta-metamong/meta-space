package com.metamong.mt.domain.member.service;

import java.util.Date;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.member.dto.request.FindMemberRequestDto;
import com.metamong.mt.domain.member.dto.request.LoginRequestDto;
import com.metamong.mt.domain.member.dto.request.OwnerSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.UserSignUpRequestDto;
import com.metamong.mt.domain.member.dto.response.LoginInfoResponseDto;
import com.metamong.mt.domain.member.exception.IdEmailAleadyExistException;
import com.metamong.mt.domain.member.exception.InvalidLoginRequestException;
import com.metamong.mt.domain.member.exception.InvalidLoginRequestType;
import com.metamong.mt.domain.member.exception.InvalidPasswordResetRequestException;
import com.metamong.mt.domain.member.exception.MemberNotFoundException;
import com.metamong.mt.domain.member.exception.PasswordNotConfirmedException;
import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.repository.jpa.MemberRepository;
import com.metamong.mt.domain.member.repository.mybatis.MemberMapper;
import com.metamong.mt.global.mail.MailAgent;
import com.metamong.mt.global.mail.MailType;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class DefaultMemberService implements MemberService {
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailAgent mailAgent;
    //private final SimpMessagingTemplate messagingTemplate; 
    private Date lastExecutionTime;
    private int roleUserCount; 
    
    @Override
    @Transactional(readOnly = true)
    public LoginInfoResponseDto findLoginInfo(LoginRequestDto dto) {
        LoginInfoResponseDto loginInfo = memberMapper.findLoginInfoByUserId(dto.getUserId())
                .orElseThrow(() -> new InvalidLoginRequestException(InvalidLoginRequestType.MEMBER_NOT_EXISTS));
        
        String encodedPassword = this.passwordEncoder.encode(dto.getPassword());
        if (encodedPassword.equals(loginInfo.getPassword())) {
            throw new InvalidLoginRequestException(InvalidLoginRequestType.PASSWORD_INCORRECT);
        }
        return loginInfo;
    }
    
    @Override
    public void saveUser(UserSignUpRequestDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordNotConfirmedException();
        }
        if(memberRepository.existsByUserIdOrEmail(dto.getUserId(), dto.getEmail())) {
        	throw new IdEmailAleadyExistException();
        }
        
        System.out.println("\n\n\n 들어오나?");
    	Member member = dto.toEntity();
        member.setPassword(this.passwordEncoder.encode(dto.getPassword()));
    	this.memberRepository.save(member);
        
	       
    }

    @Override
    public void saveOwner(OwnerSignUpRequestDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordNotConfirmedException();
        }
        Member owner = dto.toEntity();
        owner.setPassword(this.passwordEncoder.encode(dto.getPassword()));
        this.memberRepository.save(owner);
    }

	@Override
	@Transactional(readOnly = true)
	public Member findMember(String userId) {
	    return this.memberRepository.findById(userId)
	            .orElseThrow(() -> new MemberNotFoundException(userId, "회원을 찾을 수 없습니다."));
	}
	
	@Override
	public void updateRefreshToken(String userId, String refreshToken) {
	    Member member = findMember(userId);
	    member.setRefreshToken(refreshToken);
	}

//	@Override
//	public void storeRefreshToken(Member member) {
//
//        // 사용자가 존재하는 경우, refreshToken을 저장
//        if (member != null) {
//        	member.setRefreshToken(member.getRefreshToken());  
//            memberMapper.updateMember(member);  
//        } else {
//            throw new RuntimeException("사용자를 찾을 수 없습니다.");
//        }
//		
//	}
	
	@Override
    public void deleteRefreshToken(String userId) {
	    Member member = findMember(userId);
	    member.setRefreshToken(null);
    }
	
	@Override
	public void sendLoginInfoNotificationMail(FindMemberRequestDto request) {
	    switch (request.getIdOrPw()) {
	    case ID:
	        sendMailForId(request.getEmail());
	        break;
	    case PW:
	        sendMailForPassword(request.getEmail(), request.getUserId());
	    }
	}
	
	private void sendMailForId(String email) {
	    Member findMember = this.memberRepository.findByEmail(email)
	            .orElseThrow(() -> {
	                throw new MemberNotFoundException(email);
	            });
	    this.mailAgent.send(MailType.ID_NOTIFICATION, "아이디 정보", email, findMember.getUserId());
	}
	
	private void sendMailForPassword(String email, String userId) {
	    if (!this.memberRepository.existsByUserIdAndEmail(userId, email)) {
	        throw new InvalidPasswordResetRequestException();
	    }
	    this.mailAgent.send(MailType.PASSWORD_RESET_LINK, "패스워드 재설정 링크", email, "링크"); // TODO: 패스워드 재설정 보내줘야 함.
	}
	
	public void registerAnswer() {
        // 답변 등록 완료 후, 클라이언트에 메시지 전송
        //messagingTemplate.convertAndSend("/topic/answer-registered", "답변이 등록되었습니다");
    }
	
	

	@Scheduled(cron = "0 0/1 * * * ?") // 매 1분마다 실행
    public void getRoleUserCount() {
        roleUserCount = memberMapper.countRoleUserMembers();
        lastExecutionTime = new Date(); 
    }

    public String view() {
    	return "개수"+roleUserCount;
    }

    
}
