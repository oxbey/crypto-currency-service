package com.test.oril.repositories;

import com.test.oril.entities.ExchangeRateRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExchangeRateRepository extends MongoRepository<ExchangeRateRecord, String> {
	List<ExchangeRateRecord> findExchangeRateRecordsByCurr1(String currencyName);
	Page<ExchangeRateRecord> findExchangeRateRecordsByCurr1(String currencyName, PageRequest pageRequest, Sort sort);
}
