package com.test.oril.entities;

import lombok.Getter;

public enum Currency {
	BTC("BTC"),
	ETH("ETH"),
	XRP("XRP"),
	USD("USD");
	@Getter
	private final String name;

	Currency(String name) {
		this.name = name;
	}
}
