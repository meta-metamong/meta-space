package com.metamong.mt.member.repository.mybatis;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.metamong.mt.member.dto.response.LoginInfoResponseDto;
import com.metamong.mt.member.model.Member;

@Repository
@Mapper
public interface MemberMapper {
	Optional<LoginInfoResponseDto> findLoginInfoByUserId(@Param("userId") String userId);
	
	void updateMember(Member member);
	
	boolean existsAdmin();
}