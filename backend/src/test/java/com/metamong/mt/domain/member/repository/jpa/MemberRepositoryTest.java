package com.metamong.mt.domain.member.repository.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.testutil.DummyEntityGenerator;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class MemberRepositoryTest {
    
    @Autowired
    MemberRepository memberRepository;
//
//    @Test
//    @DisplayName("findByEmail() - succeed to find")
//    void findByEmail_success() {
//        // Given
//        final String email = "userid@gmail.com";
//        final String userId = "userid";
//        Member dummy = DummyEntityGenerator.generateMemberNormalUser(userId, "name", "password", email);
//        this.memberRepository.save(dummy);
//        
//        // When
//        Optional<Member> result = this.memberRepository.findByEmail(email);
//        
//        // Then
//        assertThat(result).isNotEmpty();
//        Member member = result.get();
//        assertThat(member.getUserId()).isEqualTo(userId);
//        assertThat(member.getEmail()).isEqualTo(email);
//    }
//    
//    @Test
//    @DisplayName("findByEmail() - partially match (not full match), no result - prefix")
//    void findByEmail_partiallyMatch() {
//        // Given
//        final String email = "userid@gmail.com";
//        final String userId = "userid";
//        Member dummy = DummyEntityGenerator.generateMemberNormalUser(userId, "name", "password", email);
//        this.memberRepository.save(dummy);
//        
//        // When
//        Optional<Member> result = this.memberRepository.findByEmail(email.substring(0, email.length() - 1));
//        
//        // Then
//        assertThat(result).isEmpty();
//    }
//    
//    @Test
//    @DisplayName("findByEmail() - partially matchin (not full match), no result - suffix")
//    void findByEmail_partiallyMatchin_suffix() {
//        // Given
//        final String email = "userid@gmail.com";
//        final String userId = "userid";
//        Member dummy = DummyEntityGenerator.generateMemberNormalUser(userId, "name", "password", email);
//        this.memberRepository.save(dummy);
//        
//        // When
//        Optional<Member> result = this.memberRepository.findByEmail(email.substring(1));
//        
//        // Then
//        assertThat(result).isEmpty();
//    }
//    
//    @Test
//    @DisplayName("existsByUserIdAndEmail() - exists")
//    void existsByUserIdAndEmail() {
//        // Given
//        final String userId = "user";
//        final String email = "user@gmail.com";
//        Member dummy = DummyEntityGenerator.generateMemberNormalUser(userId, "name", "1q2w3e4r", email);
//        this.memberRepository.save(dummy);
//        
//        // When
//        boolean result = this.memberRepository.existsByUserIdAndEmail(userId, email);
//        
//        // Then
//        assertThat(result).isTrue();
//    }
//    
//    static final String ENTITY_USER_ID = "user";
//    static final String ENTITY_EMAIL = "user@gmail.com";
//    
//    static Stream<Arguments> existsByUserIdOrEmail_parameterized() {
//        return Stream.of(
//                Arguments.of("userId not match, email match", ENTITY_USER_ID + "post", ENTITY_EMAIL, true),
//                Arguments.of("userId match, email not match", ENTITY_USER_ID, "pre" + ENTITY_EMAIL, true),
//                Arguments.of("Both userId and email match", ENTITY_USER_ID, ENTITY_EMAIL, true),
//                Arguments.of("Neither userId nor email match", ENTITY_USER_ID + "A", ENTITY_EMAIL + "b", false)
//        );
//    }
//    
//    @ParameterizedTest
//    @MethodSource
//    @DisplayName("existsByUserIdOrEmail() - {0} - \"{3}\" is expected.")
//    void existsByUserIdOrEmail_parameterized(String displayName, String searchUserId, String searchEmail, boolean expected) {
//        // Given
//        Member dummy = DummyEntityGenerator.generateMemberNormalUser(ENTITY_USER_ID, "name", "1q2w3e4r", ENTITY_EMAIL);
//        this.memberRepository.save(dummy);
//        
//        // When
//        boolean result = this.memberRepository.existsByUserIdOrEmail(searchUserId, searchEmail);
//        
//        // Then
//        assertThat(result).isEqualTo(expected);
//    }
}
