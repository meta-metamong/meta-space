package com.metamong.mt.domain.facility.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.facility.dto.response.FacilityResponseDto;
import com.metamong.mt.domain.facility.model.AdditionalInfo;

@Repository
@Mapper
public interface FacilityMapper {
    
    void saveAdditionalInfo(@Param("addinfo") AdditionalInfo addinfo);
    
    FacilityResponseDto findFacilityById(@Param("fctId") Long fctId);
}
