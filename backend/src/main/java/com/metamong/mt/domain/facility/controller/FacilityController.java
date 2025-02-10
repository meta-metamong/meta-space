package com.metamong.mt.domain.facility.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.metamong.mt.domain.facility.dto.constant.Order;
import com.metamong.mt.domain.facility.dto.constant.OrderBy;
import com.metamong.mt.domain.facility.dto.constant.SearchCondition;
import com.metamong.mt.domain.facility.dto.request.FacilityDeleteRequestDto;
import com.metamong.mt.domain.facility.dto.request.FacilityListRequestDto;
import com.metamong.mt.domain.facility.dto.request.FacilityRegistrationRequestDto;
import com.metamong.mt.domain.facility.dto.request.FacilityUpdateRequestDto;
import com.metamong.mt.domain.facility.dto.response.FacilityListResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityRegistrationResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityUpdateResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityListOfMemberResponseDto;
import com.metamong.mt.domain.facility.model.Category;
import com.metamong.mt.domain.facility.service.FacilityService;
import com.metamong.mt.global.apispec.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
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
        FacilityResponseDto result = this.facilityService.getFacility(facilityId);
        if (log.isDebugEnabled()) {
            log.debug("result={}", result);
        }
        return ResponseEntity.ok(
                BaseResponse.of(result, HttpStatus.OK)
        );
    }
    
    @PutMapping("/facilities/{facilityId}")
    public ResponseEntity<BaseResponse<FacilityUpdateResponseDto>> putFacility(
            @PathVariable("facilityId") Long facilityId,
            @RequestBody FacilityUpdateRequestDto requestBody
    ) {
        FacilityUpdateResponseDto result = this.facilityService.updateFacility(facilityId, requestBody);
        if (log.isDebugEnabled()) {
            log.debug("result={}", result);
        }
        return ResponseEntity.ok(
                BaseResponse.of(result, HttpStatus.OK)
        );
    }
    
    @DeleteMapping("/facilities/{facilityId}")
    public ResponseEntity<BaseResponse<Void>> deleteFacility(@PathVariable("facilityId") Long facilityId,
            @RequestBody FacilityDeleteRequestDto dto) {
        this.facilityService.deleteFacility(facilityId, dto.getPassword());
        return new ResponseEntity<>(BaseResponse.of(HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/facilities")
    public ResponseEntity<BaseResponse<FacilityListResponseDto>> getFaciltiies(
            @RequestParam(value = "order-by", defaultValue = "DISTANCE") OrderBy orderBy,
            @RequestParam(value = "order", defaultValue = "ASC") Order order,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "page-size", defaultValue = "10") int pageSize,
            @RequestParam(value = "provider-id", required = false) Long providerId,
            @RequestParam(value = "search-keyword", required = false) String searchKeyword,
            @RequestParam(value = "search-condition", required = false) SearchCondition searchCondition,
            @RequestParam(value = "upper-latitude", required = false) Double upperLatitude,
            @RequestParam(value = "lower-latitude", required = false) Double lowerLatitude,
            @RequestParam(value = "upper-longitude", required = false) Double upperLongitude,
            @RequestParam(value = "lower-longitude", required = false) Double lowerLongitude,
            @RequestParam(value = "center-latitude", required = false) Double centerLatitude,
            @RequestParam(value = "center-longitude", required = false) Double centerLongitude,
            @RequestParam(value = "cat-id", required = false) List<String> catIds
    ) {
        FacilityListRequestDto dto = FacilityListRequestDto.builder()
                .orderBy(orderBy)
                .order(order)
                .page(page)
                .pageSize(pageSize)
                .providerId(providerId)
                .searchKeyword(searchKeyword)
                .searchCondition(searchCondition)
                .upperLatitude(upperLatitude)
                .lowerLatitude(lowerLatitude)
                .upperLongitude(upperLongitude)
                .lowerLongitude(lowerLongitude)
                .centerLatitude(centerLatitude)
                .centerLongitude(centerLongitude)
                .catIds(catIds)
                .build();
        return ResponseEntity.ok(
                BaseResponse.of(this.facilityService.getFacilities(dto), HttpStatus.OK)
        );
    }
    
    @GetMapping("/members/{memId}/facilities")
    public ResponseEntity<BaseResponse<List<FacilityListOfMemberResponseDto>>> getFacilityOfMember(@PathVariable("memId") Long memId) {
        return ResponseEntity.ok(BaseResponse.of(this.facilityService.getFacilityOfMember(memId), HttpStatus.OK));
    }
}
