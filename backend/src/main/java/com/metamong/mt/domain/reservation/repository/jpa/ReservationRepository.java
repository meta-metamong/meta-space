package com.metamong.mt.domain.reservation.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metamong.mt.domain.reservation.model.Reservation;
import java.util.List;



public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    List<Reservation> findByConsId(int consId);
}