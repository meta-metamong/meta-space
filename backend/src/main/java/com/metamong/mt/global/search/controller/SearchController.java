package com.metamong.mt.global.search.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metamong.mt.global.apispec.BaseResponse;
import com.metamong.mt.global.search.dto.request.SearchRequestDto;
import com.metamong.mt.global.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;
    
    @PostMapping
    public ResponseEntity<?> saveSearchKeyword(@RequestBody SearchRequestDto requestDto){
        searchService.saveSearchKeyword(requestDto);
        return null;
    }
    
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularKeywords(){
        return ResponseEntity.ok(BaseResponse.of(searchService.getTopSearchKeywords(10), HttpStatus.OK, "인기 검색어를 조회했습니다."));
    }
}
