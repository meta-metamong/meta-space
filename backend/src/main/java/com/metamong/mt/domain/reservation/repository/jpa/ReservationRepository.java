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
    @Query("SELECT r FROM Reservation r WHERE r.rvtDate = :usageDate " +
           "AND (r.usageStartTime < :endTime AND r.usageEndTime > :startTime)")
    List<Reservation> findOverlappingReservations(@Param("usageDate") LocalDate usageDate, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

}