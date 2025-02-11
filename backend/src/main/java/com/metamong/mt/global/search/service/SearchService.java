package com.metamong.mt.global.search.service;

import com.metamong.mt.global.search.dto.request.SearchRequestDto;
import com.metamong.mt.global.search.dto.response.SearchResponseDto;

public interface SearchService {
    /**
     * 검색 시 검색어를 레디스에 저장하는 메서드
     * @param SearchRequestDto 검색어를 담은 dto
     */
    void saveSearchKeyword(SearchRequestDto dto);
    
    /**
     * 인기 검색어를 조회하는 메서드
     * @param 가져올 인기 검색어 최대 개수
     * @return Set<String> 인기 검색어
     */
    public SearchResponseDto getTopSearchKeywords(int limit);
}
