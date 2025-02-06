package com.metamong.mt.global.auth.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.metamong.mt.domain.member.dto.response.LoginInfoResponseDto;
import com.metamong.mt.global.auth.userdetails.MemberUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT를 생성하고 검증하는 서비스를 제공
 * @author Hyelin Yoo
 */
@Slf4j
@Component
public class JwtTokenProvider {

    /**
     * 토큰 암호화에 사용할 키
     */
    private static SecretKey key = Jwts.SIG.HS256.key().build();

    /**
     * 토큰 유효기간, 30분, 단위 밀리초
     */
    // private long accessTokenValidTime = 30 * 60 * 1000L;  // 30분
    //private final long refreshTokenValidity = 1000 * 60 * 60 * 24;  // 리프레시 토큰 만료 시간 (1일)
    private long accessTokenValidTime = 10 * 1000L;  // 30분
    private long refreshTokenValidTime = 30 * 24 * 60 * 60 * 1000L; // 30일

    @Autowired
    private UserDetailsService userDetailsService;
    
    /**
     * Access Token을 만들어 반환
     * @param member 사용자 정보를 저장한 객체, 클래임에 사용자 정보를 저장하기 위해 필요
     * @return 생성된 Access Token
     */
    public String generateAccessToken(UserDetails userDetails) {
    	MemberUserDetails member = (MemberUserDetails)userDetails;
        long now = System.currentTimeMillis();
        Claims claims = Jwts.claims()
                .subject(member.getUsername())  // sub
                .issuer(member.getName())     // iss
                .issuedAt(new Date(now))      // iat
                .expiration(new Date(now + accessTokenValidTime))  // exp
                .add("roles", member.getAuthorities().toArray()[0]) // roles
                .build();
        return Jwts.builder()
                .claims(claims)
                .signWith(key)  // 암호화에 사용할 키 설정
                .compact();
    }

    /**
     * Refresh Token을 만들어 반환
     * @param member 사용자 정보를 저장한 객체, 클래임에 사용자 정보를 저장하기 위해 필요
     * @return 생성된 Refresh Token
     */
    public String generateRefreshToken(UserDetails userDetails) {
    	MemberUserDetails member = (MemberUserDetails)userDetails;
        long now = System.currentTimeMillis();
        Claims claims = Jwts.claims()
                .subject(member.getUsername())  // sub
                .issuer(member.getName())     // iss
                .issuedAt(new Date(now))      // iat
                .expiration(new Date(now + refreshTokenValidTime))  // exp
                .build();
        return Jwts.builder()
                .claims(claims)
                .signWith(key)  // 암호화에 사용할 키 설정 , 여기서 암호화
                .compact();
    }

    /**
     * 토큰에서 회원 정보 추출
     * @param token 토큰
     * @return 토큰에서 사용자 아이디를 추출해서 반환
     */
    public String getMemberId(String token) {
        return this.getClaims(token).getSubject(); // generateToken()에서 subject에 userid를 담았었음
    }
    
    // 토큰에서 username 추출
    public String getUsername(String token) {
        return this.getClaims(token).getIssuer();  // Subject로 저장된 username 반환
    
    public Claims getClaims(String token) {
        // 서명 검증을 위한 비밀키를 사용하여 토큰을 파싱
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

	/**
     * Request의 Authorization Header에서 token 값을 가져옴
     * @param request 요청 객체
     * @return 토큰
     */
	public String resolveToken(HttpServletRequest request) {
	    String accessToken = request.getHeader("Authorization");
		return accessToken == null ? null : accessToken.substring("Bearer ".length());
	}
    
   
    public String resolveRefreshTokenFromCookie(HttpServletRequest request) {
        // 쿠키 배열 가져오기
        Cookie[] cookies = request.getCookies();

        // 쿠키가 없으면 null 반환
        if (cookies == null) {
            return null;
        }

        // 쿠키 배열에서 refresh_token을 찾기
        for (Cookie cookie : cookies) {
            if ("Refresh-Token".equals(cookie.getName())) {
                return cookie.getValue(); // refresh_token 쿠키 값 반환
            }
        }

        // 해당 쿠키가 없으면 null 반환
        return null;
    }
}
