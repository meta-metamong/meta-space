package com.metamong.mt.domain.member.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metamong.mt.domain.member.model.Member;


public interface MemberRepository extends JpaRepository<Member, String> {
    
    Optional<Member> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByUserIdAndEmail(String userId, String email);
    
    boolean existsByUserIdOrEmail(String userId, String email);
    
}