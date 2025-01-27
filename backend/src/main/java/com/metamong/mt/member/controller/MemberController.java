package com.metamong.mt.member.controller;

import java.time.ZoneId;
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
import com.metamong.mt.member.dto.request.LoginRequestDto;
import com.metamong.mt.member.dto.request.MemberRequestDto;
import com.metamong.mt.member.dto.request.OwnerSignRequestDto;
import com.metamong.mt.member.dto.response.LoginResponseDto;
import com.metamong.mt.member.model.Member;
import com.metamong.mt.member.service.IMemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {
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

            Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
            refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7);  // 쿠키 만료 시간 (7일)
            refreshTokenCookie.setHttpOnly(true);             // 자바스크립트 접근 불가
            refreshTokenCookie.setSecure(true);               // HTTPS에서만 전송
            refreshTokenCookie.setPath("/");                  // 모든 경로에서 유효하도록 설정
            response.addCookie(refreshTokenCookie);           // 응답에 쿠키 추가
            
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
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
    	try {

            String refreshToken = jwtTokenProvider.resolveRefreshTokenFromCookie(request);

            if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {

                String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
                memberService.removeRefreshToken(username);

    
                removeRefreshTokenFromCookie(response);

                return ResponseEntity.ok("로그아웃 성공");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("잘못된 토큰입니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("로그아웃 처리 중 오류가 발생했습니다.");
        }
    }
    
    private void removeRefreshTokenFromCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0); // 쿠키 만료
        cookie.setPath("/"); // 모든 경로에서 유효하도록 설정
        cookie.setHttpOnly(true); // 자바스크립트에서 접근할 수 없도록 설정
        cookie.setSecure(true); // HTTPS에서만 전송되도록 설정
        response.addCookie(cookie); // 응답에 쿠키 추가
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
                .birth(registerUserRequest.getBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) 
                .detailAddress(registerUserRequest.getDetail_address())
                .role("ROLE_USER")
                .postalCode(registerUserRequest.getPostal_code())
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
}
