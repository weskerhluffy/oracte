package com.task.oracte;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
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

		var result = fetchInfo(client, today, 0);
		var offset = 0;
		while (result.size() > 0) {
			log.info("Result {}", result);
			outStream.println(formatResults(result));
			offset += result.size();

			outStream.println("Press any key to continue . . . ");
			consoleScanner.nextLine();
			result = fetchInfo(client, today, offset);
		}
	}

	public List<Map<String, String>> fetchInfo(WebClient client, LocalDateTime day, Integer offset) {
		return client.get()
				.uri(uriBuilder -> uriBuilder.queryParam("dayorder", day.getDayOfWeek().getValue())
						.queryParam("$select", "applicant,location").queryParam("$limit", PAGE_SIZE)
						.queryParam("$where", "start24<'%tR' and end24>'%tR'".formatted(day, day))
						.queryParam("$order", "applicant").queryParam("$offset", offset).build())
				.retrieve()
				// XXX:
				// https://stackoverflow.com/questions/48598233/deserialize-a-json-array-to-objects-using-jackson-and-webclient
				.bodyToMono(new ParameterizedTypeReference<List<Map<String, String>>>() {
				}).block();
	}

	public String formatResults(List<Map<String, String>> foodTrucks) {
		var at = new AsciiTable();
		at.addRule();
		at.addRow(PRETTY_COLUMN_NAMES.values());
		at.addRule();
		for (Map<String, String> foodTruck : foodTrucks) {
			at.addRow(PRETTY_COLUMN_NAMES.keySet().stream().map(foodTruck::get).collect(Collectors.toList()));
			at.addRule();
		}
		return at.render();
	}

}
