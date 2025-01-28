package com.metamong.mt.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.metamong.mt.global.commons.HttpMethodOverrideFilter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Bean
	FilterRegistrationBean<HttpMethodOverrideFilter> httpMethodOverrideFilter() {
		FilterRegistrationBean<HttpMethodOverrideFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new HttpMethodOverrideFilter());
		registrationBean.addUrlPatterns("/api/*");
		return registrationBean;
	}
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 요청에 대해 CORS 허용
                .allowedOrigins("http://localhost:8080")  // 프론트엔드 도메인 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메소드
                .allowedHeaders("*")  // 모든 헤더 허용
                .allowCredentials(true);  // 자격 증명(Cookie 등) 허용
    }
}
