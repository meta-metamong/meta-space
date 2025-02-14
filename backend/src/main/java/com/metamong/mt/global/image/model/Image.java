package com.metamong.mt.global.image.model;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "image")
@SequenceGenerator(
        name = "img_pk_generator",
        sequenceName = "img_pk_seq",
        initialValue = 121,
        allocationSize = 1
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "img_attached_to")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "img_pk_generator")
    private Long imgId;
    
    private String imgPath;
    private Integer imgDisplayOrder;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    public Image(Long imgId) {
        this.imgId = imgId;
    }
    
    protected Image(String imgPath, Integer imgDisplayOrder) {
        this.imgPath = imgPath;
        this.imgDisplayOrder = imgDisplayOrder;
    }
}
