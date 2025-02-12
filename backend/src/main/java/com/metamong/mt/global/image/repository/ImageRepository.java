package com.metamong.mt.global.image.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metamong.mt.domain.member.model.MemberImage;
import com.metamong.mt.global.image.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
}
