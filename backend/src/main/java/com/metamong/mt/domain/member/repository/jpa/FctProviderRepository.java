package com.metamong.mt.domain.member.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.member.model.FctProvider;

@Repository
public interface FctProviderRepository extends JpaRepository<FctProvider, Long>{

}
