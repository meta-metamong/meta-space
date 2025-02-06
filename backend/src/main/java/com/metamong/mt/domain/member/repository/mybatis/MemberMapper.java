package com.metamong.mt.domain.member.repository.mybatis;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.member.dto.response.LoginInfoResponseDto;
import com.metamong.mt.domain.member.model.Member;

@Repository
@Mapper
public interface MemberMapper {	
	void updateMember(Member member);
	
	boolean existsAdmin();
	
	int countRoleUserMembers();
}