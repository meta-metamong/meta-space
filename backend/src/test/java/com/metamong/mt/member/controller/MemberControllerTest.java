package com.metamong.mt.member.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRegisterUserSuccess() throws Exception {
        String userRegistrationRequestBody = "{ \"userid\": \"lin97\", \"password\": \"1234567\", \"confirmPassword\": \"1234567\", \"name\": \"혜린니\", \"email\": \"777gpfls@naver.com\", \"address\": \"11\", \"phone\": \"010-1234-5678\", \"birth\": \"2021-01-21\", \"detail_address\": \"dfd\", \"postal_code\": \"12345\" }";

        mockMvc.perform(post("/api/members/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRegistrationRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("회원가입 성공"));
    }
}
