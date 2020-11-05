package com.rodmontiel.ec.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.rodmontiel.ec.ExpensesControlApplication;

import java.net.http.HttpClient;

@Configuration
public class MvcConf implements WebMvcConfigurer {
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins(
			"http://localhost:3000", "https://localhost:3000",
			"http://rodmontiel.com", "http://www.rodmontiel.com",
			"https://rodmontiel.com", "https://www.rodmontiel.com"
		).allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS");
	}

	@Bean
	@Scope("singleton")
	public Logger createLogger() {
		return LoggerFactory.getLogger(ExpensesControlApplication.class);
	}

	@Bean
	@Scope("singleton")
	public HttpClient httpClient() {
		return HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
	}

	@Bean
	@Scope("singleton")
	public ResponseCodes responseCodes() {
		return new ResponseCodes();
	}

}
