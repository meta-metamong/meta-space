package com.metamong.mt.domain.member.controller;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import com.metamong.mt.domain.member.dto.request.ConsumerSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.EmailValidationCodeRequestDto;
import com.metamong.mt.domain.member.dto.request.EmailValidationCodeTransmissionRequestDto;
import com.metamong.mt.domain.member.dto.request.FindPasswordRequestDto;
import com.metamong.mt.domain.member.dto.request.LoginRequestDto;
import com.metamong.mt.domain.member.dto.request.PasswordChangeRequestDto;
import com.metamong.mt.domain.member.dto.request.PasswordConfirmRequestDto;
import com.metamong.mt.domain.member.dto.request.ProviderSignUpRequestDto;
import com.metamong.mt.domain.member.dto.request.UpdateRequestDto;
import com.metamong.mt.domain.member.exception.InvalidLoginRequestException;
import com.metamong.mt.domain.member.service.EmailValidationService;
import com.metamong.mt.domain.member.service.MemberService;
import com.metamong.mt.global.apispec.BaseResponse;
import com.metamong.mt.global.auth.jwt.JwtTokenProvider;
import com.metamong.mt.global.auth.userdetails.MemberUserDetails;
import com.metamong.mt.global.web.cookie.CookieGenerator;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
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
    private final UserDetailsService userDetailsService;
    private final CookieGenerator cookieGenerator;
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private final EmailValidationService emailValidationService;
    
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
        Long memId = this.memberService.login(loginRequest);

        UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(memId));
        // 액세스 토큰과 리프레시 토큰 생성
        String accessToken = this.jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = this.jwtTokenProvider.generateRefreshToken(userDetails);

        // 리프레시 토큰을 DB에 저장
        this.memberService.updateRefreshToken(memId, refreshToken);

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
                .body(BaseResponse.of(memId, HttpStatus.OK, "로그인 성공"));
    }
     
    /**
	   * 시설이용자 회원가입 처리 메서드.
	   * <p>
	   * 사용자가 제공한 정보로 시설이용자 계정을 등록하며, 비밀번호 일치를 검증합니다.
	   * </p>
	   * 
	   * @param registerUserRequest 시설이용자 회원가입 요청 정보 (아이디, 비밀번호, 이름, 이메일 등)
	   * @param result BindingResult 객체 (검증 실패 시 오류를 담고 있음)
	   * @return 시설이용자 회원가입 성공 시 응답 또는 실패 시 에러 응답
	   */
	  @PostMapping("/members/consumer")
	  public ResponseEntity<?> registerUser(@Validated @RequestBody ConsumerSignUpRequestDto registerUserRequest, BindingResult result) {
	      if (result.hasErrors()) {
	          List<String> errors = result.getAllErrors().stream()
	                                      .map(error -> error.getDefaultMessage())
	                                      .collect(Collectors.toList());
	          return ResponseEntity.badRequest().body(BaseResponse.of(HttpStatus.BAD_REQUEST, errors.toString()));
	      }
	      
	      this.memberService.saveConsumer(registerUserRequest);
	
	      return ResponseEntity.ok(BaseResponse.of(HttpStatus.CREATED, "시설이용자 회원가입이 완료되었습니다."));
	  }
	
	  /**
	   * 시설제공자 회원가입 처리 메서드.
	   * <p>
	   * 시설제공자 회원의 정보(아이디, 비밀번호, 상호명 등)를 받아 회원가입을 처리합니다.
	   * </p>
	   * 
	   * @param OwnerSignRequestrequest 시설제공자 회원가입 요청 정보 (아이디, 비밀번호, 이름, 이메일 등)
	   * @return 시설제공자 회원가입 성공 시 응답
	   */
	  @PostMapping("/members/provider")
	  public ResponseEntity<?> registerOwner(@Validated @RequestBody ProviderSignUpRequestDto request) {
	      memberService.saveProvider(request);
	      return ResponseEntity.ok(BaseResponse.of(HttpStatus.CREATED, "시설제공자 회원가입이 완료되었습니다."));
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
    	 
    	 boolean isAvailable = accessToken != null && refreshToken != null;
    	 boolean isReissuable = false;
    	 try {
    		 isReissuable = !this.jwtTokenProvider.validateToken(accessToken);
    	 }catch(ExpiredJwtException e) {
    		 isReissuable = true;
    	 }catch(Exception e) {
    		 isReissuable = false;
    	 }
    	 
    	 try {
    		 isReissuable = isReissuable && this.jwtTokenProvider.validateToken(refreshToken);
    	 }catch(Exception e) {
    		 isReissuable = false;
    	 }
    	 
    	 if(isAvailable && isReissuable) {    
    		 removeRefreshTokenFromCookie(response);
    		 String memberId = jwtTokenProvider.getMemberId(refreshToken);
    		 UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);

    		 String reissuedAccessToken = this.jwtTokenProvider.generateAccessToken(userDetails);
    		 String reissuedRefreshToken = this.jwtTokenProvider.generateRefreshToken(userDetails);
    		 this.memberService.updateRefreshToken(Long.valueOf(memberId), reissuedRefreshToken);
    		 
    		 HttpHeaders headers = new HttpHeaders();
    	        headers.set(HttpHeaders.SET_COOKIE, this.cookieGenerator.generateCookie("Refresh-Token", reissuedRefreshToken).toString());
    	        headers.set("X-Access-Token", reissuedAccessToken);
    	        
    	        return ResponseEntity.ok()
    	                .headers(headers)
    	                .body(BaseResponse.of(HttpStatus.OK, "토큰 재발급 성공"));
    	 }
         
    	 return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                 .body(BaseResponse.of(refreshToken, HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."));
	  }
	  
	  @PostMapping("/members/send-validation-emails")
	  public ResponseEntity<BaseResponse<String>> sendValidationEmail(@Validated @RequestBody EmailValidationCodeTransmissionRequestDto requestBody) {
	      return ResponseEntity.ok(BaseResponse.of(this.emailValidationService.sendEmailValidationCode(requestBody.getEmail()), 
	              HttpStatus.OK, "메일을 전송했습니다."));
	  }
	  
	  @PostMapping("/members/check-email-validation")
	  public ResponseEntity<BaseResponse<String>> checkEmailValidation(@Validated @RequestBody EmailValidationCodeRequestDto requestBody) {
	      String signUpValidationCode =
	              this.emailValidationService.validateCode(requestBody.getEmail(), requestBody.getEmailValidationCode());
	      return ResponseEntity.ok(
	              BaseResponse.of(signUpValidationCode, HttpStatus.OK, "content에 signUpValidationCode가 있음")
	      );
	  }

	  @GetMapping("/test")
	  public ResponseEntity<?> testApi(HttpServletRequest request, @AuthenticationPrincipal User user){
		  MemberUserDetails userDetails = (MemberUserDetails)userDetailsService.loadUserByUsername(user.getUsername());
		  return ResponseEntity.ok(userDetails.getName());
	  }
	  
	private void removeRefreshTokenFromCookie(HttpServletResponse response) {
		  Cookie cookie = new Cookie("Refresh-Token", null);
		  cookie.setMaxAge(0); // 쿠키 만료
		  cookie.setPath("/"); // 모든 경로에서 유효하도록 설정
		  cookie.setHttpOnly(true); // 자바스크립트에서 접근할 수 없도록 설정
		  cookie.setSecure(true); // HTTPS에서만 전송되도록 설정
		  response.addCookie(cookie); // 응답에 쿠키 추가
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
    
            if (refreshToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.of(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 존재하지 않습니다."));
            } 
            String memberId = jwtTokenProvider.getMemberId(refreshToken);
            
            memberService.deleteRefreshToken(Long.parseLong(memberId));
            removeRefreshTokenFromCookie(response);

            return ResponseEntity.ok(BaseResponse.of(HttpStatus.OK, "로그아웃 성공"));
        }catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponse.of(HttpStatus.UNAUTHORIZED, "만료된 토큰"));
        } catch (SecurityException | MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponse.of(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰"));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(BaseResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "로그아웃 처리 중 오류가 발생했습니다."));
        } 
    	
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
     @PutMapping("/members")
     public ResponseEntity<?> updateMember(@AuthenticationPrincipal User user, @Valid @RequestBody UpdateRequestDto dto) {       
       return ResponseEntity.ok(BaseResponse.of(this.memberService.updateMember(Long.parseLong(user.getUsername()), dto), 
                               HttpStatus.OK, "회원 수정 성공"));
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
    public ResponseEntity<?> getMember(@PathVariable Long userId){
    	return ResponseEntity.ok(BaseResponse.of(memberService.searchMember(userId), HttpStatus.OK));
    }
    
    /**
     * 비밀번호 확인 메서드
     * <p>
     *  비밀번호 변경 전 비밀번호를 확인하는 메서드입니다.
     * </p>
     * 
     * @param 현재 비밀번호
     * @return 비밀번호 확인 성공 여부
     */
    @PostMapping("/members/password")
    public ResponseEntity<?> confirmPassword(@AuthenticationPrincipal User user, @Validated @RequestBody PasswordConfirmRequestDto dto){
        try {
            memberService.confirmPassword(Long.valueOf(user.getUsername()), dto);
            return ResponseEntity.ok(BaseResponse.of(true, HttpStatus.OK, "비밀번호 인증이 확인되었습니다."));
        }catch(InvalidLoginRequestException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponse.of(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."));
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "비밀번호 인증 중 오류가 발생했습니다."));
        }
    }
    
    /**
     * 비밀번호 변경 메서드
     * <p>
     *  새로운 비밀번호를 사용하기 위해 비밀번호를 변경하는 메서드입니다.
     * </p>
     * 
     * @param password와 passwordConfirm를 담은 객체
     * @return 비밀번호 변경 성공 여부
     */
    @PutMapping("/members/password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal User user, @Validated @RequestBody PasswordChangeRequestDto dto){
        memberService.changePassword(Long.parseLong(user.getUsername()), dto);
        return ResponseEntity.ok(BaseResponse.of(true, HttpStatus.OK, "비밀번호 변경 성공"));
    }
    
    /**
     * 회원 탈퇴 메서드
     * <p>
     *   프로필에서 회원 탈퇴 버튼을 누를 때 발생하는 요청을 처리하는 메서드입니다.
     * </p>
     * 
     * @return 삭제 처리 성공 여부
     */
    @DeleteMapping("/members")
    public ResponseEntity<?> deleteMember(@AuthenticationPrincipal User user) {
        try{
            boolean isSuccess = this.memberService.deleteMember(Long.valueOf(user.getUsername()));
            if(isSuccess) {
                return ResponseEntity.ok(BaseResponse.of(true, HttpStatus.OK, "회원이 삭제되었습니다."));
            }else {
                throw new RuntimeException();
            }
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "로그아웃 처리 중 오류가 발생했습니다."));
        }
    }

    
    /**
     * 비밀번호 찾기 버튼을 누른 후 이메일을 입력하여 재설정 링크를 전송받습니다.
     * 
     * @param email 이메일
     * @return verification 인증코드
     */
    @PostMapping("/members/find-password")
    public ResponseEntity<?> findPassword(@Validated @RequestBody FindPasswordRequestDto dto){
        return ResponseEntity.ok(BaseResponse.of(HttpStatus.OK, "재설정 링크가 이메일로 전송되었습니다."));
    }
    
    /**
     * 회원가입, 회원 정보, 예약 취소 시 사용할 은행 정보 목록입니다.
     * 
     * @return List<BankResponseDto> 은행 목록
     */
    @GetMapping("/banks")
    public ResponseEntity<?> getAllBanks(){
        return ResponseEntity.ok(BaseResponse.of(this.memberService.getAllBanks(), HttpStatus.OK, "은행 목록이 조회되었습니다."));
    }
}
