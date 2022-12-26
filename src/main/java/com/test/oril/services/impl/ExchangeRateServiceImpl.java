package com.test.oril.services.impl;

import com.opencsv.CSVWriter;
import com.test.oril.entities.ExchangeRateRecord;
import com.test.oril.exceptions.IncorrectCryptoCurrencyNameException;
import com.test.oril.repositories.ExchangeRateRepository;
import com.test.oril.services.ExchangeRateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.test.oril.entities.Currency.*;
import static java.util.Arrays.asList;

@Slf4j
@Service
@AllArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

	@Autowired
	private ExchangeRateRepository exchangeRateRepository;
	final List<String> currencies = asList(BTC.getName(), ETH.getName(), XRP.getName());

	@Override
	public void save(ExchangeRateRecord response) {
		exchangeRateRepository.insert(response);
	}

	@Override
	public Optional<ExchangeRateRecord> getRecordWithMinPrice(String currencyName) throws IncorrectCryptoCurrencyNameException {
		if (isCurrencyNameValid(currencyName)) {
			List<ExchangeRateRecord> records = getRecordsByCurrency(currencyName);
			if (records.isEmpty()) {
				log.info("Records with crypto currency name {} don't exist in db", currencyName);
				return Optional.empty();
			}
			return records.stream().min(Comparator.comparing(ExchangeRateRecord::getLprice));
		}
		throw new IncorrectCryptoCurrencyNameException();
	}

	@Override
	public Optional<ExchangeRateRecord> getRecordWithMaxPrice(String currencyName) throws IncorrectCryptoCurrencyNameException {
		if (isCurrencyNameValid(currencyName)) {
			List<ExchangeRateRecord> records = getRecordsByCurrency(currencyName);
			if (records.isEmpty()) {
				log.info("Records with crypto currency name {} don't exist in db", currencyName);
				return Optional.empty();
			}
			return records.stream().max(Comparator.comparing(ExchangeRateRecord::getLprice));
		}
		throw new IncorrectCryptoCurrencyNameException();
	}

	private boolean isCurrencyNameValid(String currencyName) {
		return currencies.contains(currencyName);
	}

	@Override
	public List<ExchangeRateRecord> getRecordsByCurrency(String currencyName) {
		return exchangeRateRepository.findExchangeRateRecordsByCurr1(currencyName);
	}

	@Override
	public List<ExchangeRateRecord> getRecordsByCurrencyWithPagination(String currencyName, int page, int size) throws IncorrectCryptoCurrencyNameException {
		if (isCurrencyNameValid(currencyName)) {
			Sort sort = Sort.by(Sort.Direction.ASC, "lprice");
			Page<ExchangeRateRecord> exchangeRateRecordPage = exchangeRateRepository.findExchangeRateRecordsByCurr1(currencyName, PageRequest.of(page, size), sort);
			return exchangeRateRecordPage.getContent();
		} throw new IncorrectCryptoCurrencyNameException();
	}

	@Override
	public File createCSVReport() throws IncorrectCryptoCurrencyNameException{
		File file = new File("ExchangeRateReport.csv");
		try (Writer writer = new FileWriter(file.getName())) {
			CSVWriter csvWriter = new CSVWriter(writer);
			for (String currency : currencies) {
				csvWriter.writeNext(createReportRecord(currency));
			}
			log.info("CSV report created at path {}", file.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	private String[] createReportRecord(String currencyName) throws IncorrectCryptoCurrencyNameException {
		try {
			String minPrice = getRecordWithMinPrice(currencyName).orElseThrow().getLprice();
			String maxPrice = getRecordWithMaxPrice(currencyName).orElseThrow().getLprice();
			return new String[]{currencyName, minPrice, maxPrice};
		} catch (NoSuchElementException exception) {
			log.warn("Absent last price for crypto currency {}", currencyName);
			throw exception;
		}
	}

}
