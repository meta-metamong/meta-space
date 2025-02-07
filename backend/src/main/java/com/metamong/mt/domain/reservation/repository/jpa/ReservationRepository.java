package com.metamong.mt.domain.reservation.repository.jpa;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.metamong.mt.domain.reservation.model.Reservation;

import jakarta.persistence.LockModeType;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r.usageStartTime, r.usageEndTime, SUM(r.usageCount) " +
            "FROM Reservation r " +
            "WHERE r.rvtDate = :usageDate " +
            "AND (r.usageStartTime < :endTime AND r.usageEndTime > :startTime) " +
            "AND r.zoneId = :zoneId " +
            "GROUP BY r.usageStartTime, r.usageEndTime")
     List<Object[]> getHourlyUsageCounts(
             @Param("usageDate") LocalDate usageDate,
             @Param("startTime") LocalTime startTime,
             @Param("endTime") LocalTime endTime,
             @Param("zoneId") Long zoneId);


}