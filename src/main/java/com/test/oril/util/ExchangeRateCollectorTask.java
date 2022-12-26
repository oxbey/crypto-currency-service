package com.test.oril.util;

import com.test.oril.entities.ExchangeRateRecord;
import com.test.oril.services.ExchangeRateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TimerTask;

import static com.test.oril.entities.Currency.*;
import static java.util.Arrays.asList;

@Slf4j
@Configuration
@AllArgsConstructor
public class ExchangeRateCollectorTask extends TimerTask {

	private RestTemplate restTemplate;
	private ExchangeRateService exchangeRateService;

	private final static String BASE_URL = "https://cex.io/api/last_price/";
	private final String BTC_URL = BASE_URL + BTC.getName() + "/" + USD.getName();
	private final String ETH_URL = BASE_URL + ETH.getName() + "/" + USD.getName();
	private final String XRP_URL = BASE_URL + XRP.getName() + "/" + USD.getName();

	private final List<String> urls = asList(BTC_URL, ETH_URL, XRP_URL);

	@Override
	public void run() {
		urls.forEach(this::saveResponse);
	}

	private void saveResponse(String url) {
		Optional<ExchangeRateRecord> optionalResponse = getResponse(url);
		if (optionalResponse.isPresent()) {
			ExchangeRateRecord response = optionalResponse.get();
			response.setCreatedAt(LocalDateTime.now());
			exchangeRateService.save(response);
		}
	}

	private Optional<ExchangeRateRecord> getResponse(String url) {
		try {
			ExchangeRateRecord response = restTemplate.getForObject(url, ExchangeRateRecord.class);
			return Optional.ofNullable(response);
		} catch (HttpClientErrorException clientErrorException) {
			return Optional.empty();
		}
	}
}
