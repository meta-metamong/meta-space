package com.metamong.mt.domain.reservation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metamong.mt.domain.reservation.dto.request.CancelRequestDto;
import com.metamong.mt.domain.reservation.dto.request.ReservationRequestDto;
import com.metamong.mt.domain.reservation.dto.request.SelectedInfoRequestDto;
import com.metamong.mt.domain.reservation.dto.response.RecommendationResponseDto;
import com.metamong.mt.domain.reservation.dto.response.RemainingCountResponseDto;
import com.metamong.mt.domain.reservation.dto.response.ReservationInfoResponseDto;
import com.metamong.mt.domain.reservation.service.ReservationService;
import com.metamong.mt.global.apispec.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final WebClient webClient;

    @GetMapping("/members/{memId}/reservations")
    public ResponseEntity<?> findReservationByConsId(@PathVariable Long memId) {
        return ResponseEntity.ok(
                BaseResponse.of(reservationService.findReservationByConsId(memId), HttpStatus.OK, "예약 목록 조회 성공"));
    }

    @GetMapping("/reservations/{reservationId}")
    public ResponseEntity<?> findReservationByRvtId(@PathVariable Long reservationId) {
        return ResponseEntity.ok(BaseResponse.of(reservationService.findReservationByRvtId(reservationId),
                HttpStatus.OK, "예약 상세 정보 불러오기 성공"));
    }
    
    @PostMapping("/reservations")
    public ResponseEntity<?> saveResevation(@Validated @RequestBody ReservationRequestDto dto) {
    	this.reservationService.saveReservation(dto);
        return ResponseEntity.ok(BaseResponse.of(HttpStatus.CREATED, "예약하기 성공"));
    }

    @PutMapping("/reservations/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId, @RequestBody CancelRequestDto dto) {
        this.reservationService.cancelReservation(reservationId, dto);
        return ResponseEntity.ok(BaseResponse.of(dto, HttpStatus.OK, "예약 취소 성공"));
    }
    
    @PostMapping("/reservations/remain")
    public ResponseEntity<?> getAvailableTimes(@RequestBody SelectedInfoRequestDto dto) {
        
        List<RemainingCountResponseDto> availableTimes = reservationService.getRemainingUsageCount(dto);
        return ResponseEntity.ok(BaseResponse.of(availableTimes, HttpStatus.OK));
    }

    @PostMapping("/recommends/{memId}")
    public Mono<RecommendationResponseDto> getRecommendations(@PathVariable Long memId)
            throws JsonProcessingException {
        List<ReservationInfoResponseDto> rvtInfo = reservationService.getTotalCount();

        Map<String, Object> rvtInfoList = new HashMap<>();
        rvtInfoList.put("reservation_info", rvtInfo);

        ObjectMapper objectMapper = new ObjectMapper();
        if (log.isDebugEnabled()) {
            log.info("Sending request with body: " + objectMapper.writeValueAsString(rvtInfoList));
        }

        return webClient.post().uri("/recommend/" + memId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(rvtInfoList) // JSON 형태로 데이터 전송
                .retrieve() // 요청을 실행하고 응답을 받음
                .bodyToMono(RecommendationResponseDto.class); // 본문을 추천 응답으로 변환
    }
}
