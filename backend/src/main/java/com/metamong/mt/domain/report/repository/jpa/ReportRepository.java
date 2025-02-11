package com.metamong.mt.domain.report.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.report.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>{

}
