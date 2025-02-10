package com.metamong.mt.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.metamong.mt.global.commons.HttpMethodOverrideFilter;
import com.metamong.mt.global.logging.LoggingInterceptor;

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
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/ai").setViewName("ai");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(new LoggingInterceptor())
	            .addPathPatterns("/api/**");
	}
}
