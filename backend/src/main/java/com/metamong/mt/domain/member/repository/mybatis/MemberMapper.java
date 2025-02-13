package com.metamong.mt.domain.member.repository.mybatis;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.member.model.Member;

@Repository
@Mapper
public interface MemberMapper {	
    
    // 관리자가 존재하는지 체크하는 쿼리	
	boolean existsAdmin();
	
	
	// 탈퇴하지 않은 회원 조회
	Member getMember(Long memId);
	
	// 회원 탈퇴
	boolean deleteMember(Long memId);
	
	Optional<Long> findMemIdByFctId(@Param("fctId") Long fctId);
	
	List<Long> findAllAdminIds();
}