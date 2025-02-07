package com.metamong.mt.domain.facility.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.metamong.mt.domain.facility.model.Zone;


public interface ZoneRepository extends JpaRepository<Zone, Long> {
    @Query("SELECT z.maxUserCount FROM Zone z WHERE z.id = :zoneId")
    Integer findMaxUserCountByZoneId(@Param("zoneId") Long zoneId);
}
