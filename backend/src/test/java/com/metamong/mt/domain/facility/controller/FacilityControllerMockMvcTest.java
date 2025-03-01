package com.metamong.mt.domain.facility.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metamong.mt.domain.facility.dto.response.FacilityRegistrationResponseDto;
import com.metamong.mt.domain.facility.dto.response.FacilityUpdateResponseDto;
import com.metamong.mt.domain.facility.dto.response.ImageUploadUrlResponseDto;
import com.metamong.mt.domain.facility.dto.response.ZoneImageUploadUrlResponseDto;
import com.metamong.mt.domain.facility.service.FacilityService;
import com.metamong.mt.global.config.BeanConfig;

@WebMvcTest(controllers = FacilityController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({
    BeanConfig.class 
})
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
                        List.of(
                                new ZoneImageUploadUrlResponseDto(
                                        1,
                                        List.of(
                                                new ImageUploadUrlResponseDto(1, "http://localhost:8080/api/files/image2.jpg")
                                        )
                                )
                        )
                ));
        
        this.mockMvc.perform(post("/api/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content("""
                        {
                            "fctName": "sampleName",
                            "fctPostalCode": "12345",
                            "fctAddress": "hello",
                            "fctDetailAddress": "World",
                            "catId": "100",
                            "provId": 3000,
                            "fctTel": "02-1234-1234",
                            "fctGuide": "Hello, World!",
                            "fctOpenTime": "12:00:00",
                            "fctCloseTime": "16:00:00",
                            "unitUsageTime": 30,
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
                                    "isSharedZone": 1,
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
                            "statusCode": 201,
                            "content": {
                                "generatedId": 10,
                                "fctImageUploadUrls": [
                                    {
                                        "order": 1,
                                        "uploadUrl": "http://localhost:8080/api/files/image1.jpg"
                                    }
                                ],
                                "zoneImageUploadUrls": [
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
                        }
                        """));
    }
    
    @Test
    @DisplayName("PUT /api/facilities/{facilityId} - success")
    void putFacility_success() throws Exception {
        when(this.facilityService.updateFacility(anyLong(), any()))
                .thenReturn(new FacilityUpdateResponseDto(
                        List.of(10L, 20L),
                        List.of(10L, 20L)));
        
        final Long facilityId = 20L;
        this.mockMvc.perform(put("/api/facilities/{facilityId}", facilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "fctName": "FCT",
                            "fctPostalCode": "01234",
                            "fctAddress": "Hello",
                            "fctDetailAddress": "World",
                            "isOpenOnHolidays": "Y",
                            "zones": {
                                "create": [
                                    {
                                        "zoneName": "Hello!",
                                        "maxUserCount": 13,
                                        "isSharedZone": "Y",
                                        "hourlyRate": 1500
                                    },
                                    {
                                        "zoneName": "Zone2",
                                        "maxUserCount": 50,
                                        "isSharedZone": "N",
                                        "hourlyRate": 3000
                                    }
                                ],
                                "update": [
                                    {
                                        "id": 5,
                                        "to": {
                                            "zoneName": "new_zone1",
                                            "maxUserCount": 13
                                        }
                                    }
                                ],
                                "delete": [100, 200, 300]
                            },
                            "addinfos": {
                                "create": [
                                    "newinfo1", "newinfo2"
                                ],
                                "update": [
                                    {
                                        "id": 500,
                                        "to": "modified1"
                                    },
                                    {
                                        "id": 501,
                                        "to": "modified2"
                                    }
                                ],
                                "delete": []
                            }
                        }
                        """))
                .andDo(print()).andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "statusCode": 200,
                            "content": {
                                "generatedZoneIds": [10, 20],
                                "generatedAdditionalInfoIds": [10, 20]
                            }
                        }
                        """));
    }
    
    @Autowired
    ObjectMapper objectMapper;
    
    @Test
    void test() throws JsonProcessingException {
        String str = objectMapper.writeValueAsString(LocalTime.of(13, 50, 10));
        System.out.println("str=" + str);
    }
}
