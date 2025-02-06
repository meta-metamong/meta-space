package com.metamong.mt.domain.reservation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metamong.mt.domain.reservation.dto.response.RecommendationResponseDto;
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

    @GetMapping("/members/{memberId}/reservations")
    public ResponseEntity<?> findReservationByConsId(@PathVariable Long memberId) {
        return ResponseEntity.ok(
                BaseResponse.of(reservationService.findReservationByConsId(memberId), HttpStatus.OK, "예약 목록 조회 성공"));
    }

    @GetMapping("/reservations/{reservationId}")
    public ResponseEntity<?> findReservationByRvtId(@PathVariable Long reservationId) {
        return ResponseEntity.ok(BaseResponse.of(reservationService.findReservationByRvtId(reservationId),
                HttpStatus.OK, "예약 상세 정보 불러오기 성공"));
    }

    @PostMapping("/recommends/{memberId}")
    public Mono<RecommendationResponseDto> getRecommendations(@PathVariable int memberId)
            throws JsonProcessingException {
        List<ReservationInfoResponseDto> rvtInfo = reservationService.getTotalCount();

        Map<String, Object> rvtInfoList = new HashMap<>();
        rvtInfoList.put("reservation_info", rvtInfo);

        ObjectMapper objectMapper = new ObjectMapper();
        log.info("Sending request with body: " + objectMapper.writeValueAsString(rvtInfoList));

        return webClient.post().uri("/recommend/" + memberId).contentType(MediaType.APPLICATION_JSON)
                .bodyValue(rvtInfoList) // JSON 형태로 데이터 전송
                .retrieve() // 요청을 실행하고 응답을 받음
                .bodyToMono(RecommendationResponseDto.class); // 본문을 추천 응답으로 변환
    }
}
