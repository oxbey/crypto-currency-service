package com.test.oril.controllers;

import com.test.oril.entities.ExchangeRateRecord;
import com.test.oril.exceptions.IncorrectCryptoCurrencyNameException;
import com.test.oril.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/cryptocurrencies")
public class ExchangeRateController {

	@Autowired
	private ExchangeRateService exchangeRateService;

	@GetMapping("/minprice")
	public ResponseEntity<ExchangeRateRecord> getMinPriceRecord(@RequestParam String currencyName) throws IncorrectCryptoCurrencyNameException {
		Optional<ExchangeRateRecord> record = exchangeRateService.getRecordWithMinPrice(currencyName);
		if (record.isPresent()) {
			return ResponseEntity.of(record);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/maxprice")
	public ResponseEntity<ExchangeRateRecord> getMaxPriceRecord(@RequestParam String currencyName) throws IncorrectCryptoCurrencyNameException {
		Optional<ExchangeRateRecord> record = exchangeRateService.getRecordWithMaxPrice(currencyName);
		if (record.isPresent()) {
			return ResponseEntity.of(record);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping
	public ResponseEntity<List<ExchangeRateRecord>> getRecords(@RequestParam String currencyName,
	                                                           @RequestParam(required = false, defaultValue = "0") int page,
	                                                           @RequestParam(required = false, defaultValue = "10") int size) throws IncorrectCryptoCurrencyNameException {
		List<ExchangeRateRecord> records = exchangeRateService.getRecordsByCurrencyWithPagination(currencyName, page, size);
		if (!records.isEmpty()) {
			return new ResponseEntity<>(records, OK);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/csv")
	public ResponseEntity<String> getReport() throws IncorrectCryptoCurrencyNameException {
		String absolutePath = exchangeRateService.createCSVReport().getAbsolutePath();
		return !absolutePath.isEmpty() ? new ResponseEntity<>(absolutePath, OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
