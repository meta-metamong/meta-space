package com.metamong.mt.domain.member.service;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.member.dto.request.ConsumerSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.LoginRequestDto;
import com.metamong.mt.domain.member.dto.request.PasswordChangeRequestDto;
import com.metamong.mt.domain.member.dto.request.ProviderSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.UpdateRequestDto;
import com.metamong.mt.domain.member.dto.response.MemberResponseDto;
import com.metamong.mt.domain.member.exception.EmailAleadyExistException;
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
    public Long login(LoginRequestDto dto) {
        Member member = null; 
        try {
            member = memberRepository.findByEmail(dto.getEmail())
            		.orElseThrow(() -> new MemberNotFoundException(dto.getEmail(), "회원을 찾을 수 없습니다."));
        } catch (MemberNotFoundException e) {
            throw new InvalidLoginRequestException(InvalidLoginRequestType.MEMBER_NOT_EXISTS, e);
        }
        
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new InvalidLoginRequestException(InvalidLoginRequestType.PASSWORD_INCORRECT);
        }
        
        return member.getMemId();
    }
    
    @Override
    public void saveConsumer(ConsumerSignUpRequestDto dto) {

        if(memberRepository.existsByEmail(dto.getEmail())) {
        	throw new EmailAleadyExistException();
        }
        
    	Member member = dto.toEntity();
        member.setPassword(this.passwordEncoder.encode(dto.getPassword()));
    	this.memberRepository.save(member);
        
	       
    }

    @Override
    public void saveProvider(ProviderSignUpRequestDto dto) {

        if(memberRepository.existsByEmail(dto.getEmail())) {
        	throw new EmailAleadyExistException();
        }
        
        Member owner = dto.toEntity();
        owner.setPassword(this.passwordEncoder.encode(dto.getPassword()));
        this.memberRepository.save(owner);
    }
    
    @Override
	public void updateRefreshToken(Long memberId, String refreshToken) {
	    Member member = getMember(memberId);
	    member.setRefreshToken(refreshToken);
	}
    
    @Override
    public void deleteRefreshToken(Long memberId) {
	    Member member = getMember(memberId);
	    member.setRefreshToken(null);
    }
    
    @Override
	@Transactional(readOnly = true)
	public Member getMember(Long memberId) {
	    return this.memberRepository.findById(memberId)
	    		.orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));
	}
    
    

	@Override
	@Transactional(readOnly = true)
	public MemberResponseDto searchMember(Long userId) {
	    Member member = getMember(userId);
	    return MemberResponseDto.builder()
								.memId(member.getMemId())
								.memName(member.getMemName())
								.email(member.getEmail())
								.memPhone(member.getMemPhone())
								.gender(member.getGender())
								.birthDate(member.getBirthDate())
								.memPostalCode(member.getMemPostalCode())
								.memDetailAddress(member.getMemDetailAddress())
								.memAddress(member.getMemAddress())
								.role(member.getRole())
								.build();
	}
	
	
	@Override
	@Transactional
	public void updateMember(Long memId, UpdateRequestDto dto) {
		Member member = getMember(memId);
		if (dto.getPassword() != null) {
			member.setPassword(this.passwordEncoder.encode(dto.getPassword()));
		}
	    member.updateInfo(dto);
	}

    @Override
    public void changePassword(Long memId, PasswordChangeRequestDto dto) {
        Member member = getMember(memId);
        if(!passwordEncoder.matches(dto.getOldPassword(), member.getPassword())) {
            throw new InvalidLoginRequestException(InvalidLoginRequestType.PASSWORD_INCORRECT);
        }else if(!dto.getNewPassword().equals(dto.getNewPasswordConfirm())) {
            throw new PasswordNotConfirmedException();
        }
        
        member.changePassword(passwordEncoder.encode(dto.getNewPassword()));
    }
	
	private void sendMailForPassword(String email) {
	    if (!this.memberRepository.existsByEmail(email)) {
	        throw new InvalidPasswordResetRequestException();
	    }
	    this.mailAgent.send(MailType.PASSWORD_RESET_LINK, "패스워드 재설정 링크", email, "링크"); // TODO: 패스워드 재설정 보내줘야 함.
	}

	@Scheduled(cron = "0 0/1 * * * ?")
    public void getRoleUserCount() {
        roleUserCount = memberMapper.countRoleUserMembers();
        lastExecutionTime = new Date(); 
    }

    public String view() {
    	return "개수"+roleUserCount;
    }

}
