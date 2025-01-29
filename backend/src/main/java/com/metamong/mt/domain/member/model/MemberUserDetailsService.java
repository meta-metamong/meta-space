package com.metamong.mt.domain.member.model;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.metamong.mt.domain.member.repository.jpa.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 로그인 시 사용하는 DTO (username으로 조회)
        Member memberInfo = this.memberRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("[" + username + "] 사용자를 찾을 수 없습니다."));

        // DB에서 가져온 역할 (role) 사용하여 권한 설정
        String[] roles = { memberInfo.getRole().name() };  // DB에서 가져온 역할
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);

        // MemberUserDetails 객체 생성
        return new MemberUserDetails(
                memberInfo.getUserId(),     // 아이디
                memberInfo.getPassword(),   // 비밀번호
                authorities,                // 권한
                memberInfo.getEmail()       // 이메일
        );
    }
}
