package com.metamong.mt.domain.facility.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.facility.model.AdditionalInfo;
import com.metamong.mt.domain.facility.model.Zone;

@Repository
@Mapper
public interface FacilityMapper {

    void saveZone(@Param("zone") Zone zone);
    
    void saveAdditionalInfo(@Param("addinfo") AdditionalInfo addinfo);
}
