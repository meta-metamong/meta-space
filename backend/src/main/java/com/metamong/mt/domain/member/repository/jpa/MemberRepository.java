package com.metamong.mt.domain.member.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.metamong.mt.domain.member.model.Member;


public interface MemberRepository extends JpaRepository<Member, String> {
    
    Optional<Member> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByUserIdAndEmail(String userId, String email);
    
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Member m WHERE m.userId = :userId OR m.email = :email")
    boolean existsByUserIdOrEmail(String userId, String email);
    
}