package com.task.oracte;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import de.vandermeer.asciitable.AsciiTable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class OracteApplication implements CommandLineRunner {

	static final String FOOD_TRUCK_API_URL = "https://data.sfgov.org/resource/jjew-r69b.json";
	@Autowired
	private FoodTruckFetcher fetcher;

	public static void main(String[] args) {
		log.info("STARTING THE APPLICATION");
		SpringApplication.run(OracteApplication.class, args);
		log.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) {
		fetcher.run(webClient(), consoleScanner(), System.out);
	}

	@Bean
	public WebClient webClient() {
		return WebClient.builder()
				// XXX: https://github.com/spring-projects/spring-framework/issues/23961
				// XXX:
				// https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/web-reactive.html#webflux-codecs-limits
				.exchangeStrategies(ExchangeStrategies.builder()
						.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024)).build())
				.baseUrl(FOOD_TRUCK_API_URL).defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	@Bean
	public Scanner consoleScanner() {
		return new Scanner(System.in);
	}

}
