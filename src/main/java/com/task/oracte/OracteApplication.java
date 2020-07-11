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

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

	private static final String FOOD_TRUCK_API_URL = "https://data.sfgov.org/resource/jjew-r69b.json";
	private static final Integer PAGE_SIZE = 10;
	private static final Map<String, String> PRETTY_COLUMN_NAMES = new LinkedHashMap<>() {
		{
			put("applicant", "NAME");
			put("location", "ADDRESS");
		}
	};

	public static void main(String[] args) {
		log.info("STARTING THE APPLICATION");
		SpringApplication.run(OracteApplication.class, args);
		log.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) {
		var today = LocalDateTime.now();

		log.info("EXECUTING : command line runner");

		var client = WebClient.builder()
				// XXX: https://github.com/spring-projects/spring-framework/issues/23961
				// XXX:
				// https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/web-reactive.html#webflux-codecs-limits
				.exchangeStrategies(ExchangeStrategies.builder()
						.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024)).build())
				.baseUrl(FOOD_TRUCK_API_URL).defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();
		var result = client.get()
				.uri(uriBuilder -> uriBuilder.queryParam("dayorder", today.getDayOfWeek().getValue())
						.queryParam("$select", "applicant,location").queryParam("$limit", PAGE_SIZE)
						.queryParam("$where", "start24<'%tR' and end24>'%s'".formatted(today, today))
						.queryParam("$order", "applicant").build())
				.retrieve()
				// XXX:
				// https://stackoverflow.com/questions/48598233/deserialize-a-json-array-to-objects-using-jackson-and-webclient
				.bodyToMono(new ParameterizedTypeReference<List<Map<String, String>>>() {
				}).block();
		var offset = 0;
		Scanner scan = new Scanner(System.in);
		while (result.size() > 0) {
			log.info("Result {}", result);
			var at = new AsciiTable();
			at.addRule();
			at.addRow(PRETTY_COLUMN_NAMES.values());
			at.addRule();
			for (Map<String, String> foodTruck : result) {
//var line = PRETTY_COLUMN_NAMES.keySet().stream().map(c -> foodTruck.get(c)).reduce("", (a, c) -> "%s\t\t%s".formatted(a, c));
				// var line = PRETTY_COLUMN_NAMES.keySet().stream().map(c -> foodTruck.get(c))
				// .collect(Collectors.joining("\t\t\t\t\t\t"));
				at.addRow(PRETTY_COLUMN_NAMES.keySet().stream().map(foodTruck::get).collect(Collectors.toList()));
				at.addRule();
			}
			System.out.println(at.render());
			offset += result.size();

			System.out.print("Press any key to continue . . . ");
			scan.nextLine();
			final int offset_ = offset;
			result = client.get()
					.uri(uriBuilder -> uriBuilder.queryParam("dayorder", today.getDayOfWeek().getValue())
							.queryParam("$select", "applicant,location").queryParam("$limit", PAGE_SIZE)
							.queryParam("$where", "start24<'%tR' and end24>'%tR'".formatted(today, today))
							.queryParam("$order", "applicant").queryParam("$offset", offset_).build())
					.retrieve()
					// XXX:
					// https://stackoverflow.com/questions/48598233/deserialize-a-json-array-to-objects-using-jackson-and-webclient
					.bodyToMono(new ParameterizedTypeReference<List<Map<String, String>>>() {
					}).block();
		}
	}

}
