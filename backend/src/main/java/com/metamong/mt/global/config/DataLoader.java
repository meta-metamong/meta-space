package com.metamong.mt.global.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.constant.Role;
import com.metamong.mt.domain.member.repository.jpa.MemberRepository;
import com.metamong.mt.domain.member.repository.mybatis.MemberMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 이미 admin 계정이 존재하지 않으면 기본값을 삽입
        if (!memberMapper.existsAdmin()) {
            String encodedPassword = passwordEncoder.encode("1234");  // 비밀번호 암호화

            // 빌더 패턴을 사용하여 admin 계정 생성
            Member admin = Member.builder()
                    .userId("admin")
                    .name("Admin")
                    .password(encodedPassword)
                    .phone("000-0000-0000")
                    .email("admin@example.com")
                    .role(Role.ROLE_ADMIN)
                    .postalCode("00000")
                    .address("서울시 예시구 예시동")
                    .detailAddress("상세 주소 예시")
                    .businessName(null)
                    .businessRegistrationNumber(null)
                    .birth(null)
                    .build();

            memberRepository.save(admin);
            System.out.println("Admin user has been created.");
        }
    }
}
