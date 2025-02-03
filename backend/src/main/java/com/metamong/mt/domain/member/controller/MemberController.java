package com.metamong.mt.domain.member.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.metamong.mt.domain.member.dto.request.FindMemberRequestDto;
import com.metamong.mt.domain.member.dto.request.LoginRequestDto;
import com.metamong.mt.domain.member.dto.request.OwnerSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.UserSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.UpdateRequestDto;
import com.metamong.mt.domain.member.dto.response.LoginInfoResponseDto;
import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.service.MemberService;
import com.metamong.mt.global.apispec.BaseResponse;
import com.metamong.mt.global.jwt.JwtTokenProvider;
import com.metamong.mt.global.web.cookie.CookieGenerator;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    private final List<WebSocketSession> sessions = new ArrayList<>(); // WebSocket 세션을 저장할 리스트

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

        // 액세스 토큰과 리프레시 토큰 생성
        String accessToken = this.jwtTokenProvider.generateAccessToken(loginInfo);
        String refreshToken = this.jwtTokenProvider.generateRefreshToken(loginInfo);

        // 리프레시 토큰을 DB에 저장
        this.memberService.updateRefreshToken(loginInfo.getUserId(), refreshToken);

        // 쿠키 생성
        ResponseCookie accessTokenResponseCookie = this.cookieGenerator.generateCookie("Ws-Access-Token", accessToken);
        ResponseCookie refreshTokenResponseCookie = this.cookieGenerator.generateCookie("Refresh-Token", refreshToken);

        // ResponseCookie를 javax.servlet.http.Cookie로 변환
        Cookie accessTokenCookie = new Cookie(accessTokenResponseCookie.getName(), accessTokenResponseCookie.getValue());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge((int) accessTokenResponseCookie.getMaxAge().getSeconds());
        accessTokenCookie.setPath("/");

        Cookie refreshTokenCookie = new Cookie(refreshTokenResponseCookie.getName(), refreshTokenResponseCookie.getValue());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge((int) refreshTokenResponseCookie.getMaxAge().getSeconds());
        refreshTokenCookie.setPath("/");

        // 쿠키를 응답에 추가
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // 액세스 토큰을 헤더에 추가 (일반 API 요청용)
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Access-Token", accessToken);

        return ResponseEntity.ok()
                .headers(headers)
                .body(BaseResponse.of(loginInfo, HttpStatus.OK, "로그인 성공"));
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

        return ResponseEntity.ok(BaseResponse.of(HttpStatus.CREATED, "일반 회원가입이 완료되었습니다."));
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
        return ResponseEntity.ok(BaseResponse.of(HttpStatus.CREATED, "업주 회원가입이 완료되었습니다."));
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
    public ResponseEntity<?> findIdPw(@RequestBody FindMemberRequestDto requestDto){
        this.memberService.sendLoginInfoNotificationMail(requestDto);
        return ResponseEntity.ok(BaseResponse.of(HttpStatus.OK, "요청하신 정보를 이메일로 전송했습니다."));
    }
    
    /**
     * 엑세스 및 리프레시 토큰 재발급 메서드
     * <p>
     * 헤더에 담겨있는 리프레시 토큰과 엑세스 토큰을 이용해 새로운 엑세스 및 리프레시 토큰을 재발급합니다.
     * </p>
     * 
     * @return 엑세스 및 리프레시 토큰
     */
    @GetMapping("/members/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){
    	 String accessToken = this.jwtTokenProvider.resolveToken(request);
    	 String refreshToken = this.jwtTokenProvider.resolveRefreshTokenFromCookie(request);
    	 System.out.println(accessToken);
    	 boolean isAvailable = accessToken != null && refreshToken != null;
    	 boolean isReissuable = !this.jwtTokenProvider.validateToken(accessToken) && this.jwtTokenProvider.validateToken(refreshToken);
    	 
    	 if(isAvailable && isReissuable) {    
    		 removeRefreshTokenFromCookie(response);
    		 
    		 Member member = this.memberService.findMember(this.jwtTokenProvider.getUserId(refreshToken));
    		 LoginInfoResponseDto loginInfo = new LoginInfoResponseDto(
    				 	member.getUserId(),
    				 	member.getName(),
    				 	member.getRole()
    		 );
    		 String reissuedAccessToken = this.jwtTokenProvider.generateAccessToken(loginInfo);
    		 String reissuedRefreshToken = this.jwtTokenProvider.generateRefreshToken(loginInfo);
    		 this.memberService.updateRefreshToken(member.getUserId(), reissuedRefreshToken);
    		 
    		 HttpHeaders headers = new HttpHeaders();
    	        headers.set(HttpHeaders.SET_COOKIE, this.cookieGenerator.generateCookie("Refresh-Token", reissuedRefreshToken).toString());
    	        headers.set("X-Access-Token", "Bearer " + reissuedAccessToken);
    	        
    	        return ResponseEntity.ok()
    	                .headers(headers)
    	                .body(BaseResponse.of(HttpStatus.OK, "토큰 재발급 성공"));
    	 }
         
    	 return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                 .body(BaseResponse.of(refreshToken, HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."));
    }
    
    /**
     * 회원 정보를 수정하는 메서드
     * 
     * <p>
     * 회원의 아이디를 통해 해당 회원의 정보를 수정합니다. 
     * 수정하려는 데이터는 요청 본문에서 전달되며, 유효성 검사를 거쳐 데이터가 처리됩니다.
     * </p>
     * 
     * @param userId 회원의 아이디 (수정하려는 회원의 고유 식별자)
     * @param dto 수정할 회원 정보가 담긴 데이터 전송 객체 (UserUpdateRequestDto)
     * @return 회원 수정 성공 시, HTTP 상태 코드 200(OK)와 함께 성공 메시지를 담은 응답
     */
    @PutMapping("/members/{userId}")
    public ResponseEntity<?> updateMember(@PathVariable String userId, @Valid @RequestBody UpdateRequestDto dto) {
    	this.memberService.updateMember(userId, dto);
    	return ResponseEntity.ok(BaseResponse.of(HttpStatus.OK, "회원 수정 성공"));
    }
    
    /**
     * 회원 데이터 조회 메서드
     * 
     * <p>
     * 	회원의 아이디를 통해 회원 정보를 조회하고, 데이터 필터링 후 응답합니다.
     * </p>
     * 
     * @param userId 회원의 아이디
     * @return 회원 데이터 (아이디, 이름, 이메일, 전화번호, 생일, 우편번호, 상세 주소, 주소)
     * @return 조회 대상이 업주라면 사업자명이랑 사업자번호도 추가됩니다.
     */
    @GetMapping("/members/{userId}")
    public ResponseEntity<?> getMember(@PathVariable String userId){
    	return ResponseEntity.ok(BaseResponse.of(memberService.getMember(userId), HttpStatus.OK));
    }
    
    /**
     * 아이디 중복 체크
     * <p>
     * 아이디를 파라미터로 입력받아, 해당 아이디를 사용하는 회원이 있으면 false를 반환하고, 아니면 true를 반환한다.
     * </p>
     * @param userId 회원 아이디
     * @return isDuplicated 중복 여부
     */
    @GetMapping("/members/dup-id/{userId}")
    public ResponseEntity<?> checkIdDuplicated(@PathVariable String userId){
    	boolean isDuplicated = this.memberService.isDuplicatedIdOrEmail(userId, "user");
    	return ResponseEntity.ok(BaseResponse.of(isDuplicated, HttpStatus.OK));
    }
    
    /**
     * 이메일 중복 체크
     * <p>
     * 이메일을 파라미터로 입력받아, 해당 이메일을 사용하는 회원이 있으면 false를 반환하고, 아니면 true를 반환한다.
     * </p>
     * @param userId 회원 아이디
     * @return isDuplicated 중복 여부
     */
    @PostMapping("/members/dup-email")
    public ResponseEntity<?> checkEmailDuplicated(@RequestBody Map<String, String> request){
    	boolean isDuplicated = this.memberService.isDuplicatedIdOrEmail(request.get("email"), "email");
    	return ResponseEntity.ok(BaseResponse.of(isDuplicated, HttpStatus.OK));
    }
    
    @GetMapping("/test")
    public ResponseEntity<?> testApi(HttpServletRequest request){
    	String accessToken = this.jwtTokenProvider.resolveToken(request);
    	String refreshToken = this.jwtTokenProvider.resolveRefreshTokenFromCookie(request);
    	if(accessToken != null && refreshToken != null) {
    		if(!this.jwtTokenProvider.validateToken(accessToken)) {
    			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    	                .body(BaseResponse.of(accessToken, HttpStatus.UNAUTHORIZED, "엑세스 토큰 만료입니다."));
    		}else if(!this.jwtTokenProvider.validateToken(refreshToken)) {
    			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    	                .body(BaseResponse.of(refreshToken, HttpStatus.UNAUTHORIZED, "리프레시 토큰 만료입니다."));
    		}
    		Map<String, String> token = new HashMap<>();
        	token.put("access", accessToken);
        	token.put("refresh", refreshToken);
        	return ResponseEntity.ok()
                    .body(BaseResponse.of(token, HttpStatus.OK, "테스트 성공"));
    	}
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.of(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."));
    }
    

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }
    
    @GetMapping("/members/roleUserCount")
    public String getRoleUserCount() {
        return memberService.view();
    }
    
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
    	
        String fileDirectory = "C:/Users/KOSA/Downloads/";
        File file = new File(fileDirectory + filename);
        
        if (!file.exists()) {
            return ResponseEntity.notFound().build();  // 파일이 없을 때 처리해놓깅
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")  
                .contentType(MediaType.parseMediaType("application/octet-stream"))  
                .body(resource);
    }
}
