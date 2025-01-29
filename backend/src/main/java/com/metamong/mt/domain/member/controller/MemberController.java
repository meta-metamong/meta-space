package com.metamong.mt.domain.member.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metamong.mt.domain.member.dto.request.FindMemberRequestDto;
import com.metamong.mt.domain.member.dto.request.LoginRequestDto;
import com.metamong.mt.domain.member.dto.request.OwnerSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.UserSignUpRequestDto;
import com.metamong.mt.domain.member.dto.response.LoginInfoResponseDto;
import com.metamong.mt.domain.member.service.MemberService;
import com.metamong.mt.global.apispec.BaseResponse;
import com.metamong.mt.global.jwt.JwtTokenProvider;
import com.metamong.mt.global.web.cookie.CookieGenerator;

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
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieGenerator cookieGenerator;

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
        LoginInfoResponseDto loginInfo = this.memberService.findLoginInfo(loginRequest);
        
        String accessToken = this.jwtTokenProvider.generateAccessToken(loginInfo);
        String refreshToken = this.jwtTokenProvider.generateRefreshToken(loginInfo);
        
        this.memberService.updateRefreshToken(loginInfo.getUserId(), refreshToken);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.SET_COOKIE, this.cookieGenerator.generateCookie("Refresh-Token", refreshToken).toString());
        headers.set("X-Access-Token", "Bearer " + accessToken);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(BaseResponse.of(HttpStatus.OK, "로그인 성공"));
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
                memberService.deleteRefreshToken(username);

    
                removeRefreshTokenFromCookie(response);

                return ResponseEntity.ok(BaseResponse.of(HttpStatus.OK, "로그아웃 성공"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.of(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "로그아웃 처리 중 오류가 발생했습니다."));
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
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserSignUpRequestDto registerUserRequest, BindingResult result) {
    	
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                                        .map(error -> error.getDefaultMessage())
                                        .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(BaseResponse.of(HttpStatus.BAD_REQUEST, errors.toString()));
        }
        
        this.memberService.saveUser(registerUserRequest);

//        try {
//            memberService.saveUser(member);
//        } catch (DuplicateKeyException e) {
//            return ResponseEntity.badRequest()
//                                 .body(new ErrorResponse(ErrorCode.USER_ALREADY_EXISTS).getMessage());
//        }

        return ResponseEntity.ok(BaseResponse.of(HttpStatus.OK, "회원가입 성공"));
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
    public ResponseEntity<?> registerOwner(@RequestBody OwnerSignUpRequestDto request) {
        memberService.saveOwner(request);
        return ResponseEntity.ok(BaseResponse.of(HttpStatus.OK, "업주 회원가입이 완료되었습니다."));
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
    public ResponseEntity<?> findMember(@RequestBody FindMemberRequestDto requestDto){
        this.memberService.sendLoginInfoNotificationMail(requestDto);
        return ResponseEntity.ok(BaseResponse.of(HttpStatus.OK, "요청하신 정보를 이메일로 전송했습니다."));
    }
}
