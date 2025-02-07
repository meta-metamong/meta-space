package com.metamong.mt.global.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metamong.mt.global.image.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
