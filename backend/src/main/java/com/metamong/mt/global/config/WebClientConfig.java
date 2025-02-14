package com.metamong.mt.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    
    @Value("${python-server-url}")
    private String pythonServierUrl;

	@Bean
	WebClient webClient() {
		return WebClient.builder()
				.exchangeStrategies(ExchangeStrategies.builder()
						.codecs(configurer -> configurer.defaultCodecs()
								.maxInMemorySize(-1))
						.build())
				.baseUrl(this.pythonServierUrl)
				.build();
	}
}
