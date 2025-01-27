package com.metamong.mt.member.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metamong.mt.global.error.ErrorCode;
import com.metamong.mt.global.error.ErrorResponse;
import com.metamong.mt.global.jwt.JwtTokenProvider;
import com.metamong.mt.member.dto.request.FindMemberRequestDto;
import com.metamong.mt.member.dto.request.LoginRequestDto;
import com.metamong.mt.member.dto.request.MemberRequestDto;
import com.metamong.mt.member.dto.request.OwnerSignRequestDto;
import com.metamong.mt.member.dto.response.LoginResponseDto;
import com.metamong.mt.member.model.Member;
import com.metamong.mt.member.service.IMemberService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IMemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인 처리 메서드.
     * <p>
     * 사용자가 입력한 아이디와 비밀번호를 확인하고, 로그인 성공 시 액세스 토큰과 리프레시 토큰을 반환합니다.
     * </p>
     * 
     * @param loginRequest 로그인 요청 정보 (아이디, 비밀번호)
     * @return 로그인 성공 시 액세스 토큰을 포함한 응답 또는 실패 시 에러 응답
     */
    @PostMapping("/members/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequestDto loginRequest) {
        try {
            LoginResponseDto member = memberService.selectLoginMember(loginRequest.getUserid());

            if (member == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body(new ErrorResponse(ErrorCode.USER_NOT_FOUND));
            }

            Member memberEntity = memberService.selectMemberEntity(member.getUserId()); 

            if (!passwordEncoder.matches(loginRequest.getPassword(), memberEntity.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body(new ErrorResponse(ErrorCode.PASSWORD_NOT_MATCH));
            }

            String accessToken = jwtTokenProvider.generateAccessToken(member);
            String refreshToken = jwtTokenProvider.generateRefreshToken(member);

            memberEntity.setRefreshToken(refreshToken);
            memberService.storeRefreshToken(memberEntity); 

            return ResponseEntity.ok()
                                 .header("Authorization", "Bearer " + accessToken)
                                 .body("로그인 성공");

        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ErrorResponse(ErrorCode.SERVER_ERROR));
        }
    }

    /**
     * 로그아웃 처리 메서드.
     * <p>
     * 사용자가 액세스 토큰을 이용해 로그아웃을 요청하며, 성공 시 리프레시 토큰을 삭제하거나 갱신합니다.
     * </p>
     * 
     * @param request HTTP 요청 객체 (액세스 토큰을 추출하기 위해 사용)
     * @return 로그아웃 성공 시 응답 또는 실패 시 에러 응답
     */
    @PostMapping("/members/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String accessToken = jwtTokenProvider.resolveToken(request);

            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                String username = jwtTokenProvider.getUsernameFromToken(accessToken);
                Member member = memberService.selectMemberEntity(username);

                if (member != null) {
                    memberService.storeRefreshToken(member);
                    logger.info("로그아웃 성공: " + username);
                    return ResponseEntity.ok("로그아웃 성공");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                         .body(new ErrorResponse(ErrorCode.USER_NOT_FOUND));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body(new ErrorResponse(ErrorCode.INVALID_TOKEN));
            }

        } catch (Exception e) {
            logger.error("로그아웃 처리 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ErrorResponse(ErrorCode.LOGOUT_FAILED));
        }
    }

    /**
     * 일반 사용자 회원가입 처리 메서드.
     * <p>
     * 사용자가 제공한 정보로 일반 사용자 계정을 등록하며, 비밀번호 일치를 검증합니다.
     * </p>
     * 
     * @param registerUserRequest 일반 사용자 회원가입 요청 정보 (아이디, 비밀번호, 이름, 이메일)
     * @param result BindingResult 객체 (검증 실패 시 오류를 담고 있음)
     * @return 회원가입 성공 시 응답 또는 실패 시 에러 응답
     */
    @PostMapping("/members/user")
    public ResponseEntity<String> registerUser(@Validated @RequestBody MemberRequestDto registerUserRequest, BindingResult result) {
    	
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                                        .map(error -> error.getDefaultMessage())
                                        .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors.toString());
        }

        if (!registerUserRequest.getPassword().equals(registerUserRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest()
                                 .body(new ErrorResponse(ErrorCode.PASSWORD_NOT_MATCH).getMessage());
        }

        String encodedPw = passwordEncoder.encode(registerUserRequest.getPassword());
        registerUserRequest.setPassword(encodedPw);

        Member member = Member.builder()
                .userid(registerUserRequest.getUserid())
                .password(encodedPw)
                .name(registerUserRequest.getName())
                .email(registerUserRequest.getEmail())
                .address(registerUserRequest.getAddress())
                .phone(registerUserRequest.getPhone())
                .birth(registerUserRequest.getBirth())
                .detail_address(registerUserRequest.getDetail_address())
                .role("ROLE_USER")
                .postal_code(registerUserRequest.getPostal_code())
                .build();

        try {
            memberService.insertMember(member);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.badRequest()
                                 .body(new ErrorResponse(ErrorCode.USER_ALREADY_EXISTS).getMessage());
        }

        return ResponseEntity.ok("회원가입 성공");
    }

    /**
     * 업주 회원가입 처리 메서드.
     * <p>
     * 업주 회원의 정보(아이디, 비밀번호, 상호명 등)를 받아 회원가입을 처리합니다.
     * </p>
     * 
     * @param OwnerSignRequestrequest 업주 회원가입 요청 정보 (아이디, 비밀번호, 이름, 이메일)
     * @return 업주 회원가입 성공 시 응답
     */
    @PostMapping("/members/owner")
    public ResponseEntity<String> registerOwner(@RequestBody OwnerSignRequestDto OwnerSignRequestrequest) {
    	
    	// 암호화 어떠케 됐지? 유효성, 비밀번호 확인, try catch
        Member member = Member.builder()
                .userid(OwnerSignRequestrequest.getUserid())
                .password(passwordEncoder.encode(OwnerSignRequestrequest.getPassword()))
                .name(OwnerSignRequestrequest.getName())
                .email(OwnerSignRequestrequest.getEmail())
                .role("ROLE_OWNER")
                .businessName(OwnerSignRequestrequest.getBusinessName())
                .businessRegistrationNumber(OwnerSignRequestrequest.getBusinessRegistrationNumber())
                .phone(OwnerSignRequestrequest.getPhone())
                .build();

        memberService.insertMember(member);
        return ResponseEntity.ok("업주 회원가입이 완료되었습니다.");
    }
    
    /**
     * 아이디 또는 비밀번호 찾기 메서드
     * <p>
     * 아이디 찾기 시엔 이름, 이메일, 비밀번호 찾기 시엔 아이디, 이름, 이메일을 입력받습니다.
     * 둘은 idOrPw라는 데이터를 통해 구분됩니다.
     * </p>
     * 
     * @param FindMemberRequestDto 회원 정보 찾기 요청 정보 (아이디-비밀번호 찾기, 이름, 이메일, 아이디 or 비밀번호 구분)
     * @return 정보 찾기 요청 처리 성공 시 응답
     */
    @PostMapping("/members/find-member")
    public ResponseEntity<String> findMember(@RequestBody FindMemberRequestDto requestDto){
    	if(memberService.findMember(requestDto)) {
    		return ResponseEntity.ok("요청하신 정보를 이메일로 전송했습니다.");    		
    	}else {
    		return ResponseEntity.ok("인증 요청을 실패했습니다.");
    	}
    }
}
