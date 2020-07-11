package com.task.oracte;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class OracteApplication implements CommandLineRunner {

	static final String FOOD_TRUCK_API_URL = "https://data.sfgov.org/resource/jjew-r69b.json";
	@Autowired
	private FoodTruckFetcher fetcher;

	public static void main(String[] args) {
		log.info("STARTING THE APPLICATION");
		// XXX: https://stackoverflow.com/questions/60922845/maven-exec-plugin-with-preview-features	
		SpringApplication.run(OracteApplication.class, args);
		log.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) {
		try {
		fetcher.run(webClient(), consoleScanner(), System.out);
		}catch (WebClientException e) {
			log.error("Could not fetch foodtruck data, exiting");
		}
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
