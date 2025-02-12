package com.metamong.mt.domain.reservation.dto.request;

import java.util.List;

import com.metamong.mt.domain.reservation.dto.response.ReservationResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ReservationListRequestDto<T> {
    private List<ReservationResponseDto> rvtInfo;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private int totalElements;
}
