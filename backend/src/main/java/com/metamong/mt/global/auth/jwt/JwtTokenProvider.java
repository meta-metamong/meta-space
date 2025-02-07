package com.metamong.mt.global.auth.jwt;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.metamong.mt.global.auth.userdetails.MemberUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
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
	
	private String secretKey;
	private SecretKey key;  

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
 	
    	String secretKey = environment.getProperty("jwt.secret.key")+"dsfsdf";
    	
        String encodedKey  = Base64.getEncoder().encodeToString(secretKey.getBytes());  

        if (encodedKey != null) {
            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
            
            if (decodedKey.length < 32) {
                log.error("JWT Secret Key is too short. It must be at least 256 bits (32 bytes).");
                throw new IllegalArgumentException("JWT Secret Key is too short. It must be at least 256 bits.");
            }
            this.key = Keys.hmacShaKeyFor(decodedKey);
        } else {
            log.warn("JWT Secret Key is not set. Using a temporary default key.");
            byte[] defaultKey = new byte[32]; 
            this.key = new SecretKeySpec(defaultKey, "HmacSHA256");
        }
    }


    /**
     * 토큰 유효기간, 30분, 단위 밀리초
     */
    private long accessTokenValidTime = 30 * 60 * 1000L;  // 30분
    private long refreshTokenValidTime = 14 * 24 * 60 * 60 * 1000L; // 14일
    	
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
                .signWith(key, SignatureAlgorithm.HS256)  // 암호화에 사용할 키 설정
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
    }
    
    public Claims getClaims(String token) {
        // 서명 검증을 위한 비밀키를 사용하여 토큰을 파싱
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * 토큰의 유효성과 만료 일자 확인
     * @param token 토큰
     * @return 토큰이 유효한지 확인, 유효하면 true 반환
     */
    public boolean validateToken(String token) {    	
        try {
        	this.getClaims(token);
        	return true;
        } catch(Exception e) {
        	throw e;
        }
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
