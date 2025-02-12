package com.metamong.mt.global.scheduler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.metamong.mt.domain.admin.dto.response.WeekReservationDto;
import com.metamong.mt.domain.admin.repository.redis.RedisAdminVolatileCodeRepository;
import com.metamong.mt.domain.admin.service.AdminService;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
@Service
public class SchedulerService {
    private final AdminService adminService;
    private final RedisAdminVolatileCodeRepository redisAdminVolatileCodeRepository;

    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);


//    @Scheduled(cron = "0 0 0 * * *")  // 매일 자정
    @Scheduled(cron = "0 0/1 * * * ?")  // 테스트용
    public void performDailyTasks() {
    	try {

    	List<WeekReservationDto> weeklyData = adminService.getRedisReservationsThisWeek();
    	
    	redisAdminVolatileCodeRepository.set("weeklyData", weeklyData, 24, TimeUnit.HOURS); 
    	
    	System.out.println("주간 예약 데이터를 Redis에 저장했습니다.");
    	} catch (Exception e) {
    		System.out.println("주간 예약 데이터를 Redis에 저장하는 중 오류 발생");
        }
    	
    }
}
