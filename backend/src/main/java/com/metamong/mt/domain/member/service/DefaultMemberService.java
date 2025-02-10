package com.metamong.mt.domain.member.service;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.member.dto.request.ConsumerSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.LoginRequestDto;
import com.metamong.mt.domain.member.dto.request.PasswordChangeRequestDto;
import com.metamong.mt.domain.member.dto.request.PasswordConfirmRequestDto;
import com.metamong.mt.domain.member.dto.request.ProviderSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.UpdateRequestDto;
import com.metamong.mt.domain.member.dto.response.MemberResponseDto;
import com.metamong.mt.domain.member.exception.EmailAleadyExistException;
import com.metamong.mt.domain.member.exception.IllegalSignUpRequestException;
import com.metamong.mt.domain.member.exception.InvalidLoginRequestException;
import com.metamong.mt.domain.member.exception.InvalidLoginRequestType;
import com.metamong.mt.domain.member.exception.InvalidPasswordResetRequestException;
import com.metamong.mt.domain.member.exception.MemberNotFoundException;
import com.metamong.mt.domain.member.exception.PasswordNotConfirmedException;
import com.metamong.mt.domain.member.model.Account;
import com.metamong.mt.domain.member.model.FctProvider;
import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.constant.Role;
import com.metamong.mt.domain.member.repository.jpa.AccountRepository;
import com.metamong.mt.domain.member.repository.jpa.FctProviderRepository;
import com.metamong.mt.domain.member.repository.jpa.MemberRepository;
import com.metamong.mt.domain.member.repository.mybatis.MemberMapper;
import com.metamong.mt.global.constant.BooleanAlt;
import com.metamong.mt.global.mail.MailAgent;
import com.metamong.mt.global.mail.MailType;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class DefaultMemberService implements MemberService {
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;
    private final FctProviderRepository providerRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailAgent mailAgent;
    private final EmailValidationService emailValidationService;
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
    public boolean isValidPassword(Long memId, String password) {
        // TODO: 다 가져오지 말고 패스워드만 체크해야 함
        Member member = this.memberRepository.findById(memId)
                .orElseThrow(() -> new MemberNotFoundException(String.valueOf(memId)));
        return passwordEncoder.matches(password, member.getPassword());
    }
    
    @Override
    public void saveConsumer(ConsumerSignUpRequestDto dto) {
        if(memberRepository.existsByEmail(dto.getEmail())) {
        	throw new EmailAleadyExistException();
        }
               
        if (!this.emailValidationService.isValidSignUpValidationCode(dto.getEmail(), dto.getSignUpValidationCode())) {
            throw new IllegalSignUpRequestException("Not valid signup");
        }
        
    	Member member = dto.toEntity();
        member.setPassword(this.passwordEncoder.encode(dto.getPassword()));
    	this.memberRepository.save(member);
    }

    @Override
    @Transactional
    public void saveProvider(ProviderSignUpRequestDto dto) {
        if(memberRepository.existsByEmail(dto.getEmail())) {
        	throw new EmailAleadyExistException();
        }
        
        if (!this.emailValidationService.isValidSignUpValidationCode(dto.getEmail(), dto.getSignUpValidationCode())) {
            throw new IllegalSignUpRequestException("Not valid signup");
        }
        
        // 회원 저장
        Member member = dto.toEntity();
        member.setPassword(this.passwordEncoder.encode(dto.getPassword()));
        member.setIsDel(BooleanAlt.N);
        Member savedMember = this.memberRepository.saveAndFlush(member);
        
        // 시설 제공자 데이터 저장
        FctProvider provider = dto.toProvider();
        provider.setProvId(savedMember.getMemId());
        this.providerRepository.save(provider);
        
        // 계좌 정보 저장
        Account account = dto.toAccount();
        account.setProvId(savedMember.getMemId());        
        this.accountRepository.save(account);
    }
    
    @Override
	public void updateRefreshToken(Long memId, String refreshToken) {
        Member member = getMemberByRepository(memId);
	    member.setRefreshToken(refreshToken);
	}
    
    @Override
    public void deleteRefreshToken(Long memId) {
	    Member member = getMemberByRepository(memId);
	    member.setRefreshToken(null);
    }
    
    @Override
	@Transactional(readOnly = true)
	public Member getMemberByMapper(Long memId) {
	    Member member = this.memberMapper.getMember(memId);
	    if(member == null) {
	        throw new MemberNotFoundException("회원을 찾을 수 없습니다.");
	    }
	    return member;
	}
    
    @Override
    @Transactional(readOnly = true)
    public Member getMemberByRepository(Long memId) {
        return memberRepository.findById(memId)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));
    }
    
    
    @Override
    @Transactional (readOnly = true)
    public FctProvider getProvider(Long memId) {
        return this.providerRepository.findById(memId)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));
    }
    
    @Override
    @Transactional (readOnly = true)
    public Account getAccount(Long memId) {
        return this.accountRepository.findById(memId)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));
    }

	@Override
	@Transactional(readOnly = true)
	public MemberResponseDto searchMember(Long memId) {
	    Member member = getMemberByMapper(memId);
	    FctProvider provider = null;
	    Account account = null;
	    
	    if(member.getRole().equals(Role.ROLE_PROV)) {
	        provider = this.getProvider(memId);
	        account = this.getAccount(memId);
	    }
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
                                .bizName(provider == null ? null : provider.getBizName())
                                .bizRegNum(provider == null ? null : provider.getBizRegNum())
                                .bankCode(provider == null ? null : account.getBankCode())
                                .accountNumber(provider == null ? null : account.getAccountNumber())
                                .balance(provider == null ? null : account.getBalance())
                                .build();	   
	}
	
	
	@Override
	@Transactional
	public void updateMember(Long memId, UpdateRequestDto dto) {
		Member member = getMemberByMapper(memId);
	    member.updateInfo(dto.toMember());
	    if(member.getRole().equals(Role.ROLE_PROV)) {
	        FctProvider provider = this.getProvider(memId);
	        provider.updateInfo(dto.toProvider());
	        Account account = this.getAccount(memId);
	        if(!dto.getAccountNumber().equals(account.getAccountNumber())) {
	            account.updateInfo(dto.toAccount());
	        }
	    }
	    memberRepository.save(member);
	}
	
	@Override
	@Transactional
	public boolean deleteMember(Long memId) {
	    if(!memberRepository.existsById(memId)) {
	        return false;
	    }
	    this.memberMapper.deleteMember(memId);
	    return true;
	}
	
	@Override
    public void confirmPassword(Long memId, PasswordConfirmRequestDto dto) {
	    Member member = getMemberByMapper(memId);
        if(!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new InvalidLoginRequestException(InvalidLoginRequestType.PASSWORD_INCORRECT);
        }
    }

    @Override
    public void changePassword(Long memId, PasswordChangeRequestDto dto) {
        Member member = getMemberByRepository(memId);
        if(!dto.getNewPassword().equals(dto.getNewPasswordConfirm())) {
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

	@Scheduled(cron = "0 0/5 * * * ?")
    public void getRoleUserCount() {
        roleUserCount = memberMapper.countRoleUserMembers();
        lastExecutionTime = new Date(); 
    }

    public String view() {
    	return "개수"+roleUserCount;
    }

}
