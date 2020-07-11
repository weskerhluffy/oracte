package com.task.oracte;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import de.vandermeer.asciitable.AsciiTable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FoodTruckFetcher {
	private static final Integer PAGE_SIZE = 10;
	private static final Map<String, String> PRETTY_COLUMN_NAMES = new LinkedHashMap<>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1788194901956766836L;

		{
			put("applicant", "NAME");
			put("location", "ADDRESS");
		}
	};

	public void run(WebClient client, Scanner consoleScanner, PrintStream outStream) {
		var today = LocalDateTime.now();

		log.info("EXECUTING : command line runner");

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
			outStream.println(at.render());
			offset += result.size();

			outStream.print("Press any key to continue . . . ");
			consoleScanner.nextLine();
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
