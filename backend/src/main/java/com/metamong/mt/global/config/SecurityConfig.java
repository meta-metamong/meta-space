package com.metamong.mt.global.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.metamong.mt.global.auth.filter.NoAuthPrincipalFinderFilter;
import com.metamong.mt.global.auth.jwt.JwtAuthenticationFilter;
import com.metamong.mt.global.auth.jwt.JwtTokenProvider;
import com.metamong.mt.global.config.constant.HttpRequestAuthorizationDefinition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    
    private final AuthenticationManager jwtAuthenticationManager;
    private final UserDetailsService userDetailsService;
    
    @Value("${client.origin}")
    private String clientOrigin;
	
	@Bean
	@Profile("no-auth")
	SecurityFilterChain noAuthFilterChain(HttpSecurity http) throws Exception {
	    http = commonConfiguration(http);
	    http.authorizeHttpRequests((request) -> {
            request.requestMatchers("/api/**").permitAll();
        });
	    http.addFilterBefore(new NoAuthPrincipalFinderFilter(this.userDetailsService, this.jwtTokenProvider), AuthorizationFilter.class);
	    return http.build();
	}

	@Bean
	@Profile("!no-auth")
	SecurityFilterChain mainFilterChain(HttpSecurity http) throws Exception {
		http = commonConfiguration(http);
		
		// 토큰을 사용하는 경우 인가를 적용한 URI 설정

		http.authorizeHttpRequests((registry) -> {
		    HttpRequestAuthorizationDefinition.defineRequestMatcher(registry);
		});

		// Spring Security JWT 필터 로드
		http.addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationManager, jwtTokenProvider),
		        UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	private HttpSecurity commonConfiguration(HttpSecurity http) throws Exception {
	    http.securityMatcher("/api/**");
	    http.csrf(csrfConfig -> csrfConfig.disable());
	    
	    http.cors(corsConfig -> corsConfig.configurationSource(cors(this.clientOrigin)));
	    
	    // Session 기반의 인증을 사용하지 않고 JWT를 이용하여서 인증 

        // 세션 비활성화
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http;
	}
	
	private CorsConfigurationSource cors(String clientOrigin) {
        CorsConfiguration configuration = new CorsConfiguration();
        log.trace("allowedOrigin={}", clientOrigin);
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