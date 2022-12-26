package com.test.oril.services;

import com.test.oril.entities.ExchangeRateRecord;
import com.test.oril.exceptions.IncorrectCryptoCurrencyNameException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public interface ExchangeRateService {

	void save(ExchangeRateRecord response);
	Optional<ExchangeRateRecord> getRecordWithMinPrice(String currencyName) throws IncorrectCryptoCurrencyNameException;
	Optional<ExchangeRateRecord> getRecordWithMaxPrice(String currencyName) throws IncorrectCryptoCurrencyNameException;
	List<ExchangeRateRecord> getRecordsByCurrency(String currencyName);
	List<ExchangeRateRecord> getRecordsByCurrencyWithPagination(String currencyName, int page, int size) throws IncorrectCryptoCurrencyNameException;
	File createCSVReport() throws IncorrectCryptoCurrencyNameException;
}
