package com.metamong.mt.domain.facility.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "additional_info")
@SequenceGenerator(
        name = "addinfo_pk_generator",
        sequenceName = "addinfo_pk_seq",
        initialValue = 1,
        allocationSize = 1
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class AdditionalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addinfo_pk_generator")
    private Long addinfoId;
    
    private Long fctId;
    private String addinfoDesc;
}
