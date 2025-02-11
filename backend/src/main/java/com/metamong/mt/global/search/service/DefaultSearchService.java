package com.metamong.mt.global.search.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.metamong.mt.global.search.dto.request.SearchRequestDto;
import com.metamong.mt.global.search.dto.response.SearchResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultSearchService implements SearchService{
    private static final String POPULAR_SEARCHES_KEY = "popularSearches";
    private final StringRedisTemplate redisTemplate;
    
    @Override
    public void saveSearchKeyword(SearchRequestDto dto) {
        redisTemplate.opsForZSet().incrementScore(POPULAR_SEARCHES_KEY, dto.getKeyword(), 1);
    }
    
    @Override
    public SearchResponseDto getTopSearchKeywords(int limit) {
        return SearchResponseDto.builder()
                .popularKeywords(redisTemplate.opsForZSet().reverseRange(POPULAR_SEARCHES_KEY, 0, limit - 1))
                .build();
    }
}
