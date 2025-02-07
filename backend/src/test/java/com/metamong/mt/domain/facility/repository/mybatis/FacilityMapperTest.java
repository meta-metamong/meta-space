package com.metamong.mt.domain.facility.repository.mybatis;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.annotation.Transactional;
import com.metamong.mt.domain.facility.dto.response.FacilityResponseDto;
import com.metamong.mt.domain.facility.dto.response.ZoneResponseDto;
import com.metamong.mt.domain.facility.model.AdditionalInfo;
import com.metamong.mt.domain.facility.model.Category;
import com.metamong.mt.domain.facility.model.Facility;
import com.metamong.mt.domain.facility.model.FacilityImage;
import com.metamong.mt.domain.facility.model.Zone;
import com.metamong.mt.domain.facility.model.ZoneImage;
import com.metamong.mt.domain.facility.repository.jpa.AdditionalInfoRepository;
import com.metamong.mt.domain.facility.repository.jpa.CategoryRepository;
import com.metamong.mt.domain.facility.repository.jpa.FacilityRepository;
import com.metamong.mt.domain.facility.repository.jpa.ZoneRepository;
import com.metamong.mt.domain.member.model.constant.Gender;
import com.metamong.mt.domain.member.model.constant.Role;
import com.metamong.mt.domain.member.repository.jpa.MemberRepository;
import com.metamong.mt.global.constant.BooleanAlt;
import com.metamong.mt.global.image.repository.ImageRepository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class FacilityMapperTest {
    
    @Autowired
    FacilityMapper facilityMapper;
    
    @Autowired
    CategoryRepository categoryRepository;
    
    @Autowired
    FacilityRepository facilityRepository;
    
    @Autowired
    ZoneRepository zoneRepository;
    
    @Autowired
    AdditionalInfoRepository additionalInfoRepository;
    
    @Autowired
    ImageRepository imageRepository;
    
    @Autowired
    MemberRepository memberRepository;
    
    @Autowired
    DataSource dataSource;
    
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("findFacilityById - success")
    @Transactional
    void findFacilityById_success() throws Exception {
        log.info("em class={}", this.em.getClass());
        // Given
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DataSourceUtils.getConnection(this.dataSource);
            stmt = conn.prepareStatement("DELETE FROM member");
            stmt.executeUpdate();
        } finally {
            stmt.close();
            DataSourceUtils.releaseConnection(conn, this.dataSource);
        }
        
        try {
            conn = DataSourceUtils.getConnection(this.dataSource);
            String sql = """
                    INSERT INTO member (
                        mem_id,
                        email,
                        mem_name,
                        password,
                        mem_phone,
                        birth_date,
                        gender,
                        mem_postal_code,
                        mem_address,
                        mem_detail_address,
                        role
                    ) VALUES (
                        mem_pk_seq.NEXTVAL,
                        ?,
                        ?,
                        ?,
                        ?,
                        ?,
                        ?,
                        ?,
                        ?,
                        ?,
                        ?
                    )
                    """;
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "test@gmail.com");
            stmt.setString(2, "조나단");
            stmt.setString(3, "1q2w3e4r");
            stmt.setString(4, "010-1234-1234");
            stmt.setDate(5, new Date(System.currentTimeMillis()));
            stmt.setString(6, Gender.M.name());
            stmt.setString(7, "01234");
            stmt.setString(8, "address");
            stmt.setString(9, "detail address");
            stmt.setString(10, Role.ROLE_PROV.name());
            stmt.executeUpdate();
        } finally {
            stmt.close();
            DataSourceUtils.releaseConnection(conn, this.dataSource);
        }
        
        try {
            conn = DataSourceUtils.getConnection(this.dataSource);
            stmt = conn.prepareStatement("""
                    INSERT INTO bank (bank_code, bank_name)
                    VALUES ('014', 'MY_BANK')
                    """);
            stmt.executeUpdate();
        } finally {
            stmt.close();
            DataSourceUtils.releaseConnection(conn, this.dataSource);
        }
        
        Long generatedProvId;
        ResultSet rs = null;
        try {
            conn = DataSourceUtils.getConnection(this.dataSource);
            stmt = conn.prepareStatement("SELECT mem_pk_seq.CURRVAL AS generated_prov_id FROM DUAL");
            rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Something wrong");
            }
            generatedProvId = rs.getLong("generated_prov_id");
        } finally {
            rs.close();
            stmt.close();
            DataSourceUtils.releaseConnection(conn, this.dataSource);
        }
        
        try {
            conn = DataSourceUtils.getConnection(this.dataSource);
            stmt = conn.prepareStatement("""
                    INSERT INTO fct_provider (
                        prov_id,
                        biz_name,
                        biz_reg_name,
                        bank_code,
                        prov_account,
                        prov_account_owner
                    ) VALUES (
                        ?,
                        ?,
                        ?,
                        ?,
                        ?,
                        ?
                    )
                    """);
            stmt.setLong(1, generatedProvId);
            stmt.setString(2, "sample biz name");
            stmt.setString(3, "asdf");
            stmt.setString(4, "014");
            stmt.setString(5, "110-123-123456");
            stmt.setString(6, "MyMy");
            stmt.executeUpdate();
        } finally {
            stmt.close();
            DataSourceUtils.releaseConnection(conn, this.dataSource);
        }
        
        Category category = new Category("100", "Category1");
        category = this.categoryRepository.save(category);
        Facility facility = Facility.builder()
                .cat(category)
                .provId(generatedProvId)
                .fctName("facility")
                .fctPostalCode("01234")
                .fctDetailAddress("haleaow")
                .fctAddress("Hello")
                .fctTel("02-1235-1234")
                .fctGuide("No more man")
                .openOnHolidays(BooleanAlt.Y)
                .fctOpenTime(LocalDateTime.of(LocalDate.of(1990, 1, 1), LocalTime.of(12, 0)))
                .fctCloseTime(LocalDateTime.of(LocalDate.of(1990, 1, 1), LocalTime.of(18, 0)))
                .unitUsageTime(30)
                .fctLatitude(34.51624)
                .fctLongitude(127.16315)
                .build();
        List<FacilityImage> facilityImages = List.of(
                new FacilityImage("http://localhost:8080/resources/images/f-image1", 1, facility),
                new FacilityImage("http://localhost:8080/resources/images/f-image2", 2, facility),
                new FacilityImage("http://localhost:8080/resources/images/f-image3", 3, facility)
        );
        facilityImages.forEach((fi) -> facility.addFctImage(fi));
        this.facilityRepository.save(facility);
        
        Zone zone1 = Zone.builder()
                .fctId(facility.getFctId())
                .zoneName("zone1")
                .maxUserCount(30)
                .isSharedZone(BooleanAlt.Y)
                .hourlyRate(5000)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        List<ZoneImage> zoneImages1 = List.of(
                new ZoneImage("http://localhost:8080/resources/images/z1-image1", 1, zone1),
                new ZoneImage("http://localhost:8080/resources/images/z1-image2", 2, zone1),
                new ZoneImage("http://localhost:8080/resources/images/z1-image3", 3, zone1)
        );
        zoneImages1.forEach((zi) -> zone1.addZoneImage(zi));
        this.zoneRepository.save(zone1);
        Zone zone2 = Zone.builder()
                .fctId(facility.getFctId())
                .zoneName("zone2")
                .maxUserCount(30)
                .isSharedZone(BooleanAlt.Y)
                .hourlyRate(10000)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        List<ZoneImage> zoneImages2 = List.of(
                new ZoneImage("http://localhost:8080/resources/images/z2-image1", 1, zone2),
                new ZoneImage("http://localhost:8080/resources/images/z2-image2", 2, zone2),
                new ZoneImage("http://localhost:8080/resources/images/z2-image3", 3, zone2)
        );
        zoneImages2.forEach((zi) -> zone2.addZoneImage(zi));
        this.zoneRepository.save(zone2);
        
        AdditionalInfo addinfo1 = AdditionalInfo.builder()
                .fctId(facility.getFctId())
                .addinfoDesc("hello")
                .build();
        this.additionalInfoRepository.save(addinfo1);
        
        this.em.flush();
        
        // When
        FacilityResponseDto result = this.facilityMapper.findFacilityById(facility.getFctId());
        log.info("result={}", result);
        
        // Then
        assertThat(result.getCatId()).isEqualTo(category.getCatId());
        assertThat(result.getFctImages()).size().isEqualTo(facilityImages.size());
        assertThat(result.getZones()).size().isEqualTo(List.of(zone1, zone2).size());
        assertThat(result.getZones().stream().map(ZoneResponseDto::getZoneId))
                .containsExactlyInAnyOrder(zone1.getZoneId(), zone2.getZoneId());
    }
}
