package com.metamong.mt.domain.member.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.member.dto.response.BankResponseDto;

@Repository
@Mapper
public interface BankMapper {

    /**
     * 은행 정보 목록 조회 쿼리입니다.
     * @return 은행 정보 리스트
     */
    List<BankResponseDto> findAllBanks();
    
    /**
     * 은행 코드로 은행 데이터를 조회하는 쿼리입니다.
     * @param bankCode 은행코드
     * @return 은행 정보
     */
    BankResponseDto findNameByCode(String bankCode);
}
