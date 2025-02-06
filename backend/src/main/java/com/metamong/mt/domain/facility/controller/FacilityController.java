package com.metamong.mt.domain.facility.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metamong.mt.domain.facility.dto.request.FacilityRegistrationRequestDto;
import com.metamong.mt.domain.facility.dto.response.FacilityRegistrationResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityResponseDto;
import com.metamong.mt.domain.facility.service.FacilityService;
import com.metamong.mt.global.apispec.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FacilityController {
    private final FacilityService facilityService;
    
    @PostMapping("/facilities")
    public ResponseEntity<BaseResponse<FacilityRegistrationResponseDto>> postFacility(
            @Valid @RequestBody FacilityRegistrationRequestDto requestBody) {
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(BaseResponse.of(this.facilityService.registerFacility(requestBody), HttpStatus.CREATED));
    }
    
    @GetMapping("/facilities/{facilityId}")
    public ResponseEntity<BaseResponse<FacilityResponseDto>> getFacility(@PathVariable("facilityId") Long facilityId) {
        return ResponseEntity.ok(
                BaseResponse.of(this.facilityService.getFacility(facilityId), HttpStatus.OK)
        );
    }
}
