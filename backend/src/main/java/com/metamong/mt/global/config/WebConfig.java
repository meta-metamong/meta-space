package com.metamong.mt.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
}
