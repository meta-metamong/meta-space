package com.metamong.mt.domain.reservation.repository.mybatis;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.metamong.mt.domain.facility.model.Category;
import com.metamong.mt.domain.facility.model.Facility;
import com.metamong.mt.domain.facility.model.Zone;
import com.metamong.mt.domain.member.model.FctProvider;
import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.constant.Gender;
import com.metamong.mt.domain.member.model.constant.Role;
import com.metamong.mt.domain.reservation.dto.mapper.FindConsIdWithReservationTimeMapperDto;
import com.metamong.mt.domain.reservation.model.Reservation;
import com.metamong.mt.global.constant.BooleanAlt;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@Slf4j
public class ReservationMapperTest {

    @Autowired
    ReservationMapper reservationMapper;
    
    @Autowired
    EntityManager entityManager;
    
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 })
    @DisplayName("시작 시간이 현재 시각에서 {0} 시간 이내인 예약의 예약자 ID 가져오기")
    void fetchConsIdWithReservationTimeIsLeftLessThanTheGivenTime(int testCase) {
        Member prov = Member.builder()
                .email("prov@gmail.com")
                .memName("prov")
                .password("1q2w3e4r")
                .memPhone("010-1234-1234")
                .gender(Gender.M)
                .birthDate(LocalDate.of(1997, 9, 16))
                .memPostalCode("12345")
                .memAddress("addr")
                .memDetailAddress("daddr")
                .role(Role.ROLE_PROV)
                .build();
        
        this.entityManager.persist(prov);
        
        FctProvider fctProvider = FctProvider.builder()
                .provId(prov.getMemId())
                .bizName("BIZ")
                .bizRegNum("1234-123456")
                .build();
        
        this.entityManager.persist(fctProvider);
        
        Category cat = new Category("600", "CAT");
        this.entityManager.persist(cat);
        cat = this.entityManager.find(Category.class, cat.getCatId());
        
        Facility facility = Facility.builder()
                .cat(cat)
                .provId(prov.getMemId())
                .fctName("FCT")
                .fctPostalCode("12345")
                .fctAddress("ADDR")
                .fctDetailAddress("DADDR")
                .fctTel("02-1234-1234")
                .fctGuide("GUIDE")
                .openOnHolidays(BooleanAlt.Y)
                .fctOpenTime(LocalTime.of(12, 0))
                .fctCloseTime(LocalTime.of(18, 0))
                .unitUsageTime(60)
                .fctLatitude(37.152441)
                .fctLongitude(128.134135)
                .build();
        
        this.entityManager.persist(facility);
        
        Zone zone = Zone.builder()
                .fctId(facility.getFctId())
                .zoneName("ZONE")
                .maxUserCount(1000)
                .isSharedZone(BooleanAlt.Y)
                .hourlyRate(5000)
                .build();
        
        this.entityManager.persist(zone);
        
        LocalDateTime criteria = LocalDateTime.now();
        List<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            Member cons = Member.builder()
                    .email("cons" + i + "@gmail.com")
                    .memName("cons" + i)
                    .password("1q2w3e4r")
                    .memPhone("010-1234-12" + (i < 10 ? "0" + i : i))
                    .gender(Gender.M)
                    .birthDate(LocalDate.of(1997, 9, 16))
                    .memPostalCode("123" + (i < 10 ? "0" + i : i))
                    .memAddress("addr" + i)
                    .memDetailAddress("daddr")
                    .role(Role.ROLE_CONS)
                    .build();
            
            this.entityManager.persist(cons);
            
            int num = i + 1;
            LocalDateTime time = criteria.plusHours(num);
            Reservation reservation = Reservation.builder()
                    .consId(cons.getMemId())
                    .zoneId(zone.getZoneId())
                    .rvtDate(time.toLocalDate())
                    .usageStartTime(LocalTime.of(time.getHour(), time.getMinute()))
                    .usageEndTime(LocalTime.of(time.getHour(), time.getMinute()).plusHours(1))
                    .usageCount(50)
                    .build();
            
            this.entityManager.persist(reservation);
            
            reservations.add(reservation);
        }
        
        this.entityManager.flush();
        
        // When
        List<Long> result = this.reservationMapper.findConsIdWithLeftReservationTime(
                new FindConsIdWithReservationTimeMapperDto(testCase, criteria)
        );
        
        // Then
        log.info("criteria={}", criteria);
        LocalDateTime before = criteria.plusHours(testCase);
        LocalDate beforeDate = before.toLocalDate();
        LocalTime beforeTime = LocalTime.of(before.getHour(), before.getMinute());
        log.info("beforeDate={}", beforeDate);
        log.info("beforeTime={}", beforeTime);
        log.info("rvtDate={}", reservations.stream().map(Reservation::getRvtDate).toList());
        log.info("times={}", reservations.stream().map(Reservation::getUsageStartTime).toList());
        log.info("consIds={}", reservations.stream().map(Reservation::getConsId).toList());
        
        Long[] filteredReservations = reservations.stream()
                .filter((rvt) -> rvt.getRvtDate().isBefore(beforeDate)
                        || (rvt.getRvtDate().equals(beforeDate) && (rvt.getUsageStartTime().isBefore(beforeTime) || rvt.getUsageStartTime().equals(beforeTime))))
                .map(Reservation::getConsId)
                .toArray(Long[]::new);
        
        log.info("filteredReservations={}", Arrays.toString(filteredReservations));
        
        assertThat(result).containsExactly(filteredReservations);
    }
    
    @Test
    @DisplayName("예약 ID로 시설 제공자의 ID 가져오기 - success")
    void fetchProvIdByRvtId_success() {
        Member prov = Member.builder()
                .email("prov@gmail.com")
                .memName("prov")
                .password("1q2w3e4r")
                .memPhone("010-1234-1234")
                .gender(Gender.M)
                .birthDate(LocalDate.of(1997, 9, 16))
                .memPostalCode("12345")
                .memAddress("addr")
                .memDetailAddress("daddr")
                .role(Role.ROLE_PROV)
                .build();
        
        this.entityManager.persist(prov);
        
        FctProvider fctProvider = FctProvider.builder()
                .provId(prov.getMemId())
                .bizName("BIZ")
                .bizRegNum("1234-123456")
                .build();
        
        this.entityManager.persist(fctProvider);
        
        Category cat = new Category("600", "CAT");
        this.entityManager.persist(cat);
        cat = this.entityManager.find(Category.class, cat.getCatId());
        
        Facility facility = Facility.builder()
                .cat(cat)
                .provId(prov.getMemId())
                .fctName("FCT")
                .fctPostalCode("12345")
                .fctAddress("ADDR")
                .fctDetailAddress("DADDR")
                .fctTel("02-1234-1234")
                .fctGuide("GUIDE")
                .openOnHolidays(BooleanAlt.Y)
                .fctOpenTime(LocalTime.of(12, 0))
                .fctCloseTime(LocalTime.of(18, 0))
                .unitUsageTime(60)
                .fctLatitude(37.152441)
                .fctLongitude(128.134135)
                .build();
        
        this.entityManager.persist(facility);
        
        Zone zone = Zone.builder()
                .fctId(facility.getFctId())
                .zoneName("ZONE")
                .maxUserCount(1000)
                .isSharedZone(BooleanAlt.Y)
                .hourlyRate(5000)
                .build();
        
        this.entityManager.persist(zone);
        
        LocalDateTime criteria = LocalDateTime.now();
        Member cons = Member.builder()
                .email("cons@gmail.com")
                .memName("cons")
                .password("1q2w3e4r")
                .memPhone("010-1234-1223")
                .gender(Gender.M)
                .birthDate(LocalDate.of(1997, 9, 16))
                .memPostalCode("12345")
                .memAddress("addr")
                .memDetailAddress("daddr")
                .role(Role.ROLE_CONS)
                .build();
        
        this.entityManager.persist(cons);
        
        LocalDateTime time = criteria.plusHours(24);
        Reservation reservation = Reservation.builder()
                .consId(cons.getMemId())
                .zoneId(zone.getZoneId())
                .rvtDate(time.toLocalDate())
                .usageStartTime(LocalTime.of(time.getHour(), time.getMinute()))
                .usageEndTime(LocalTime.of(time.getHour(), time.getMinute()).plusHours(1))
                .usageCount(50)
                .build();
        
        this.entityManager.persist(reservation);
        
        this.entityManager.flush();
        
        // When
        Long provId = this.reservationMapper.findProvIdByRvtId(reservation.getRvtId())
                .orElseThrow(RuntimeException::new);
        
        // Then
        assertThat(provId).isEqualTo(facility.getProvId());
    }
}
