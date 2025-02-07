package com.metamong.mt.domain.facility.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.facility.dto.mapper.FacilityUpdateMapperDto;
import com.metamong.mt.domain.facility.dto.mapper.ZoneUpdateMapperDto;
import com.metamong.mt.domain.facility.dto.response.FacilityResponseDto;
import com.metamong.mt.domain.facility.model.AdditionalInfo;

@Repository
@Mapper
public interface FacilityMapper {
    
    void saveAdditionalInfo(@Param("addinfo") AdditionalInfo addinfo);
    
    FacilityResponseDto findFacilityById(@Param("fctId") Long fctId);
    
    void updateFacilityById(@Param("dto") FacilityUpdateMapperDto dto);
    
    void updateZoneById(@Param("dto") ZoneUpdateMapperDto dto);
    
    void deleteAdditionalInfosByIds(@Param("ids") List<Long> ids);
}
