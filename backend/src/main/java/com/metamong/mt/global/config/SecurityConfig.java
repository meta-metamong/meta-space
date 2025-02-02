package com.metamong.mt.global.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.metamong.mt.global.jwt.JwtAuthenticationFilter;
import com.metamong.mt.global.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
	private final List<String> permitAllEndpoints;
	
	@Bean
	@Profile("no-auth")
	SecurityFilterChain noAuthFilterChain(HttpSecurity http) throws Exception {
	    http = commonConfiguration(http);
	    http.cors((corsConfig) -> corsConfig.configurationSource(cors("http://localhost:3000")));
	    http.authorizeHttpRequests((request) -> {
            request.requestMatchers("/**").permitAll();
        });
	    return http.build();
	}
	
	@Bean
	@Profile("prod")
	SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
	    http = commonConfiguration(http);
	    return http.build();
	}

	@Bean
	@ConditionalOnMissingBean(SecurityFilterChain.class)
	SecurityFilterChain localDevSecurityFilterChain(HttpSecurity http) throws Exception {
		http = commonConfiguration(http);
		
		http.cors(corsConfig -> corsConfig.configurationSource(cors("http://localhost:3000")));
		
		// 토큰을 사용하는 경우 인가를 적용한 URI 설정

		http.authorizeHttpRequests(auth -> {
				auth.requestMatchers("/file/**").hasRole("ADMIN");
				auth.requestMatchers("/css/**", "/js/**", "/images.**").permitAll();
				permitAllEndpoints.forEach(endpoint -> auth.requestMatchers(endpoint).permitAll());
				auth.anyRequest().authenticated();
			});

		// Spring Security JWT 필터 로드
		http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, permitAllEndpoints),
		        UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	private HttpSecurity commonConfiguration(HttpSecurity http) throws Exception {
	    http.csrf(csrfConfig -> csrfConfig.disable());
	    
	    // Session 기반의 인증을 사용하지 않고 JWT를 이용하여서 인증 

        // 세션 비활성화
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http;
	}
	
	private CorsConfigurationSource cors(@Value("${client.origin}") String clientOrigin) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(clientOrigin));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("X-Access-Token");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
	@Bean
	@Profile("!prod")
	PasswordEncoder noOpPasswordEncoder() {
	    return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	@Profile("prod")
	PasswordEncoder bcryptPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}