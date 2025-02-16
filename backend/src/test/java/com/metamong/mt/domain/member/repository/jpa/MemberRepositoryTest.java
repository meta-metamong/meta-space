package com.metamong.mt.domain.member.repository.jpa;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.constant.Gender;
import com.metamong.mt.domain.member.model.constant.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class MemberRepositoryTest {
    
    @Autowired
    MemberRepository memberRepository;
    
    @Spy
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Test
    @DisplayName("시설 이용자 데이터 저장 및 조회 테스트")
    void saveAndFindConsumer() {
        // Given (데이터 준비)
        Member member = Member.builder()
                              .email("testkhs@naver.com")
                              .memName("김한수")
                              .password(passwordEncoder.encode("1234"))
                              .memPhone("010-4118-0614")
                              .gender(Gender.M)
                              .birthDate(LocalDate.of(1999, 6, 14))
                              .memPostalCode("24209")
                              .memDetailAddress("강원도 춘천시 소양강로 104")
                              .memAddress("101-702")
                              .role(Role.ROLE_CONS)
                              .build();
        Member saved = memberRepository.save(member);

        // When (조건 설정)
        Optional<Member> found = memberRepository.findById(saved.getMemId());
        
     
        // Then (검증)
        assertThat(found).isPresent();
        assertThat(found.get().getEmail().equals("testkhs@naver.com"));
    }
}
