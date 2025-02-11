package com.metamong.mt.global.search.dto.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SearchResponseDto {
    private Set<String> popularKeywords;
}
