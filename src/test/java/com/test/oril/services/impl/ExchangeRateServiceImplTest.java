package com.test.oril.services.impl;

import com.test.oril.entities.ExchangeRateRecord;
import com.test.oril.exceptions.IncorrectCryptoCurrencyNameException;
import com.test.oril.repositories.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExchangeRateServiceImplTest {

	private final ExchangeRateRepository exchangeRateRepository = mock(ExchangeRateRepository.class);
	private final ExchangeRateServiceImpl service = new ExchangeRateServiceImpl(exchangeRateRepository);

	private final List<ExchangeRateRecord> responses = asList(
		createResponse("12.12", "BTC"),
		createResponse("345.98", "BTC"),
		createResponse("65.55", "ETH"),
		createResponse("0.23", "ETH"),
		createResponse("123.12", "XRP"),
		createResponse("543.32", "XRP")
		);

	private final List<ExchangeRateRecord> responsesBtc = asList(
		createResponse("12.12", "BTC"),
		createResponse("345.98", "BTC")
	);

	private final List<ExchangeRateRecord> responsesEth = asList(
		createResponse("65.55", "ETH"),
		createResponse("0.23", "ETH")
	);

	private final List<ExchangeRateRecord> responsesXrp = asList(
		createResponse("123.12", "XRP"),
		createResponse("543.32", "XRP")
	);

	@BeforeEach
	public void setUp() {
		when(exchangeRateRepository.findExchangeRateRecordsByCurr1("BTC")).thenReturn(responsesBtc);
		when(exchangeRateRepository.findExchangeRateRecordsByCurr1("ETH")).thenReturn(responsesEth);
		when(exchangeRateRepository.findExchangeRateRecordsByCurr1("XRP")).thenReturn(responsesXrp);
	}

	@Test
	void getRecordWithMinPrice() throws IncorrectCryptoCurrencyNameException {
		String minPriceBTC = service.getRecordWithMinPrice("BTC").get().getLprice();
		String expectedMinPriceBtc = "12.12";
		assertEquals(expectedMinPriceBtc, minPriceBTC);

		String minPriceETH = service.getRecordWithMinPrice("ETH").get().getLprice();
		String expectedMinPriceEth = "0.23";
		assertEquals(expectedMinPriceEth, minPriceETH);

		String minPriceXRP = service.getRecordWithMinPrice("XRP").get().getLprice();
		String expectedMinPriceXrp = "123.12";
		assertEquals(expectedMinPriceXrp, minPriceXRP);

		when(exchangeRateRepository.findExchangeRateRecordsByCurr1("BTC")).thenReturn(Collections.emptyList());
		assertEquals(Optional.empty(), service.getRecordWithMinPrice("BTC"));

		assertThrows(IncorrectCryptoCurrencyNameException.class, () -> service.getRecordWithMinPrice("INCORRECT").get().getLprice());

	}

	@Test
	void getRecordWithMaxPrice() throws IncorrectCryptoCurrencyNameException {
		String maxPriceBTC = service.getRecordWithMaxPrice("BTC").get().getLprice();
		String expectedMaxPriceBtc = "345.98";
		assertEquals(expectedMaxPriceBtc, maxPriceBTC);

		String maxPriceETH = service.getRecordWithMaxPrice("ETH").get().getLprice();
		String expectedMaxPriceEth = "65.55";
		assertEquals(expectedMaxPriceEth, maxPriceETH);

		String maxPriceXRP = service.getRecordWithMaxPrice("XRP").get().getLprice();
		String expectedMaxPriceXrp = "543.32";
		assertEquals(expectedMaxPriceXrp, maxPriceXRP);

		when(exchangeRateRepository.findExchangeRateRecordsByCurr1("ETH")).thenReturn(Collections.emptyList());
		assertEquals(Optional.empty(), service.getRecordWithMaxPrice("ETH"));

		assertThrows(IncorrectCryptoCurrencyNameException.class, () -> service.getRecordWithMaxPrice("INCORRECT").get().getLprice());
	}

	private ExchangeRateRecord createResponse(String lprice,
	                                          String curr1) {
		ExchangeRateRecord response = new ExchangeRateRecord();
		response.setLprice(lprice);
		response.setCurr1(curr1);
		response.setCurr2("USD");
		response.setId(UUID.randomUUID().toString());
		return response;
	}
}