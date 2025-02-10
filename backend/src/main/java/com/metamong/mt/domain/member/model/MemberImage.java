package com.metamong.mt.domain.member.model;

import com.metamong.mt.global.image.model.Image;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("M")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberImage extends Image {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_id")
    private Member member;
    
    public MemberImage(String imgPath, Integer imgDisplayOrder, Member member) {
        super(imgPath, imgDisplayOrder);
        this.member = member;
    }

    @Override
    public String toString() {
        return "MemberImage [" + super.toString() + ", member.memId=" + member.getMemId() + "]";
    }
}
