package com.metamong.mt.domain.member.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.metamong.mt.domain.member.model.constant.Gender;
import com.metamong.mt.domain.member.model.constant.Role;
import com.metamong.mt.global.constant.BooleanAlt;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="member")
@SequenceGenerator(
        name = "mem_pk_generator",
        sequenceName = "mem_pk_seq",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="mem_pk_generator")
    @Column(name="mem_id")
    private Long memId;
    
    @Column(name="email", nullable=false, unique=true)
    private String email;
    
    @Column(name="mem_name", nullable=false)
    private String memName;
    
    @Column(name="password")
    private String password;
    
    @Column(name="mem_phone")
    private String memPhone;
    
    @Enumerated(EnumType.STRING)
    @Column(name="gender", length=1)
    private Gender gender;

    @Column(name="birth_date")
    private LocalDate birthDate;
    
    @Column(name="mem_postal_code")
    private String memPostalCode;
    
    @Column(name="mem_detail_address")
    private String memDetailAddress;
    
    @Column(name="mem_address")
    private String memAddress;
    
    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private Role role;
    
    @Column(name="refresh_token")
    private String refreshToken;
    
    @CreationTimestamp
    @Column(name="created_at")
    @ColumnDefault(value="SYSDATE")
    private LocalDateTime createdAt;
    
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name="mem_banned_until")
    private LocalDateTime memBannedUntil;
    
    @Column(name="is_del", length=1)
    @Enumerated(EnumType.STRING)
    private BooleanAlt isDel;
    
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    MemberImage memImage;
    
    public void updateInfo(Member dto) {
        this.memName = dto.getMemName();
        this.memPhone = dto.getMemPhone();
        this.gender = dto.getGender();
        this.birthDate = dto.getBirthDate();
        this.memPostalCode = dto.getMemPostalCode();
        this.memAddress = dto.getMemAddress();
        this.memDetailAddress = dto.getMemDetailAddress();
    }
    
    public void changePassword(String password) {
        this.password = password;
    }
}