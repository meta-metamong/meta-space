package com.metamong.mt.domain.member.repository.mybatis;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.metamong.mt.domain.facility.model.Category;
import com.metamong.mt.domain.facility.model.Facility;
import com.metamong.mt.domain.facility.model.FacilityState;
import com.metamong.mt.domain.facility.repository.jpa.CategoryRepository;
import com.metamong.mt.domain.facility.repository.jpa.FacilityRepository;
import com.metamong.mt.domain.member.model.FctProvider;
import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.constant.Gender;
import com.metamong.mt.domain.member.model.constant.Role;
import com.metamong.mt.domain.member.repository.jpa.FctProviderRepository;
import com.metamong.mt.domain.member.repository.jpa.MemberRepository;
import com.metamong.mt.global.constant.BooleanAlt;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@Slf4j
class MemberMapperTest {
    
    @Autowired
    MemberMapper memberMapper;
    
    @Autowired
    MemberRepository memberRepository;
    
    @Autowired
    FctProviderRepository fctProviderRepository;
    
    @Autowired
    CategoryRepository categoryRepository;
    
    @Autowired
    FacilityRepository facilityRepository;
    
    @Autowired
    EntityManager entityManager;
//
//    @Test
//    @DisplayName("findLoginInfoByUserId() - 성공")
//    void findLoginInfoByUserId_successs() {
//        // Given
//        Member dummy = DummyEntityGenerator.generateMember();
//        String dummyId = dummy.getUserId();
//        String dummyName = dummy.getName();
//        Role dummyRole = dummy.getRole();
//        this.memberRepository.save(dummy);
//        
//        // When
//        LoginInfoResponseDto result = this.memberMapper.findLoginInfoByUserId(dummyId).get();
//        
//        // Then
//        assertThat(result.getUserId()).isEqualTo(dummyId);
//        assertThat(result.getName()).isEqualTo(dummyName);
//        assertThat(result.getRole()).isEqualTo(dummyRole);
//    }
    
    @Test
    @DisplayName("findMemIdByFctId() - success")
    void findMemIdByFctId_success() {
        // Given
        Member member = Member.builder()
                .email("test@gmail.com")
                .memName("test")
                .password("1q2w3e4r")
                .memPhone("010-1234-1234")
                .gender(Gender.M)
                .birthDate(LocalDate.of(1997, 9, 16))
                .memPostalCode("12345")
                .memDetailAddress("daddr")
                .memAddress("addr")
                .role(Role.ROLE_PROV)
                .build();
        member = this.memberRepository.save(member);
        
        FctProvider fctProvider = FctProvider.builder()
                .provId(member.getMemId())
                .bizName("BIZ")
                .bizRegNum("1234-152414")
                .build();
        fctProvider = this.fctProviderRepository.save(fctProvider);
        
        Category category = new Category("011", "CAT");
        category = this.categoryRepository.save(category);
        
        Facility facility = Facility.builder()
                .cat(category)
                .fctName("FCT")
                .provId(fctProvider.getProvId())
                .fctPostalCode("41242")
                .fctAddress("hello")
                .fctDetailAddress("goodbye")
                .fctTel("02-1234-1234")
                .fctGuide("Why so serious")
                .openOnHolidays(BooleanAlt.N)
                .fctOpenTime(LocalTime.of(12, 0))
                .fctCloseTime(LocalTime.of(18, 5))
                .unitUsageTime(50)
                .fctState(FacilityState.REG_APPROVED)
                .fctLatitude(36.1343141)
                .fctLongitude(128.315135)
                .build();
        facility = this.facilityRepository.save(facility);
        
        this.entityManager.flush();
        
        // When
        Optional<Long> result = this.memberMapper.findMemIdByFctId(facility.getFctId());
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.get()).isEqualTo(member.getMemId());
    }
}
