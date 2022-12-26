package com.test.oril.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document
public class ExchangeRateRecord {

	@JsonIgnore
	@Id
	private String id;
	private String lprice;
	private String curr1;
	private String curr2;
	@JsonIgnore
	private LocalDateTime createdAt;
}
