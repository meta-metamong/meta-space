package com.metamong.mt.domain.facility.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metamong.mt.domain.facility.model.Facility;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
}
