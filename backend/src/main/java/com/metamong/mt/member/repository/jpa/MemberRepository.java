package com.metamong.mt.member.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metamong.mt.member.model.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}
