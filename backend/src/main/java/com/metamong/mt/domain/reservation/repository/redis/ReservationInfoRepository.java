package com.metamong.mt.domain.reservation.repository.redis;

import java.util.Map;

public interface ReservationInfoRepository {
    void saveReservationInfo(Map<String, Object> rvtInfoList);
    
    Map<String, Object> findReservationInfo();
}
