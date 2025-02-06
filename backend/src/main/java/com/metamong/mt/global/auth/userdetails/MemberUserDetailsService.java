package com.metamong.mt.global.auth.userdetails;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.repository.jpa.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {
	
	@Autowired
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memId) throws UsernameNotFoundException {
        // 로그인 시 사용하는 DTO (username으로 조회)
        Member memberInfo = this.memberRepository.findById(Long.parseLong(memId))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // DB에서 가져온 역할 (role) 사용하여 권한 설정
        String[] roles = { memberInfo.getRole().name() };  // DB에서 가져온 역할
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);

        // MemberUserDetails 객체 생성
        return new MemberUserDetails(
                String.valueOf(memberInfo.getMemId()),     // 아이디
                memberInfo.getPassword(),   // 비밀번호
                authorities,                // 권한
                memberInfo.getMemName()       // 이메일
        );
    }
}
