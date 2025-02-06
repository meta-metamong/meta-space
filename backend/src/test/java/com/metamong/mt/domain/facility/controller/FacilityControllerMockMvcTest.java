package com.metamong.mt.domain.facility.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.metamong.mt.domain.facility.dto.response.FacilityRegistrationResponseDto;
import com.metamong.mt.domain.facility.dto.response.ImageUploadUrlResponseDto;
import com.metamong.mt.domain.facility.service.FacilityService;

@WebMvcTest(FacilityController.class)
@AutoConfigureMockMvc(addFilters = false)
class FacilityControllerMockMvcTest {
    
    @Autowired
    MockMvc mockMvc;
    
    @MockitoBean
    FacilityService facilityService;

    @Test
    @DisplayName("POST /api/facilities - success")
    void postFacility_success() throws Exception {
        when(this.facilityService.registerFacility(any()))
                .thenReturn(new FacilityRegistrationResponseDto(
                        10L,
                        List.of(
                                new ImageUploadUrlResponseDto(1, "http://localhost:8080/api/files/image1.jpg")
                        ),
                        Map.of(
                                1, List.of(new ImageUploadUrlResponseDto(1, "http://localhost:8080/api/files/image2.jpg"))
                        )
                ));
        
        this.mockMvc.perform(post("/api/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "fctName": "sampleName",
                            "fctPostcalCode": "12345",
                            "fctAddress": "hello",
                            "fctDetailAddress": "World",
                            "catId": 15,
                            "provId": 3000,
                            "fctTel": "02-1234-1234",
                            "fctGuide": "Hello, World!",
                            "fctOpenTime": "1980-09-05T12:00:00.000",
                            "fctCloseTime": "1980-09-05T16:00:00.000",
                            "unitUsgaeTime": 30,
                            "images": [
                                {
                                    "fileType": "jpg",
                                    "order": 1
                                }
                            ],
                            "zones": [
                                {
                                    "zoneNo": 1,
                                    "zoneName": "zone1",
                                    "maxUserCount": 10,
                                    "isSharedZone": true,
                                    "hourlyRate": 5000,
                                    "images": [
                                        {
                                            "fileType": "jpg",
                                            "order": 1
                                        }
                                    ]
                                }
                            ],
                            "addinfos": [
                                "info1",
                                "info2"
                            ]
                        }
                        """))
                .andDo(print()).andDo(log())
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                            "generatedId": 10,
                            "fctImageUploadUrls": [
                                {
                                    "order": 1,
                                    "uploadUrl": "http://localhost:8080/api/files/image1.jpg"
                                }
                            ],
                            "zoneImageUploadUrlResponseDtosByZoneNo": [
                                {
                                    "zoneNo": 1,
                                    "uploadUrls": [
                                        {
                                            "order": 1,
                                            "uploadUrl": "http://localhost:8080/api/files/image2.jpg"
                                        }
                                    ]
                                }
                            ]
                        }
                        """));
    }
}
