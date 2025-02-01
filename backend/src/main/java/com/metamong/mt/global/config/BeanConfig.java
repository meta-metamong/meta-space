package com.metamong.mt.global.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.metamong.mt.domain.member.model.constant.IdOrPw;
import com.metamong.mt.global.file.local.LocalFileUploader;
import com.metamong.mt.global.jackson.idorpw.IdOrPwDeserializer;
import com.metamong.mt.global.mail.MailAgent;
import com.metamong.mt.global.mail.MailAgentMock;
import com.metamong.mt.global.mail.MailMessageFormatter;
import com.metamong.mt.global.web.cookie.CookieGenerator;
import com.metamong.mt.global.web.cookie.DefaultCookieGenerator;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class BeanConfig {

    @Bean
    @ConditionalOnMissingBean(CookieGenerator.class)
    DefaultCookieGenerator defaultCookieGenerator(@Value("${client.domain}") String clientDomain) {
        return new DefaultCookieGenerator(clientDomain);
    }

    @Bean
    @ConditionalOnMissingBean(MailAgent.class)
    MailAgentMock mailAgentMock(List<MailMessageFormatter> mailMessageFormatters) {
        return new MailAgentMock(mailMessageFormatters);
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));

        SimpleModule idOrPwDeserializerModule = new SimpleModule();
        idOrPwDeserializerModule.addDeserializer(IdOrPw.class, new IdOrPwDeserializer());
        objectMapper.registerModule(idOrPwDeserializerModule);

        return objectMapper;
    }

    @Bean
    @Profile("!prod")
    LocalFileUploader localFileUploader(ServletContext servletContext) {
        String rootPath = servletContext.getRealPath("/").replaceAll("\\\\", "/");
        log.debug("local file upload root path = {}", rootPath);
        return new LocalFileUploader(rootPath);
    }

    @Bean
    List<String> permitAllEndpoints(){
		return List.of(
				"/api/members/user",
				"/api/members/owner",
				"/api/members/login",
				"/api/members/find-member",
				"/api/members/reissue",
				"/api/members/{id}",
				"/api/members/dup-id/{userId}",
				"/api/members/dup-email",
				"/actuator/health",
				"/ws/**"
			);
	}
    
}
