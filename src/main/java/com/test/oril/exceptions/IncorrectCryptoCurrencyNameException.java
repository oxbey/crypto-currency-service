package com.test.oril.exceptions;

public class IncorrectCryptoCurrencyNameException extends Exception {
	public IncorrectCryptoCurrencyNameException() {
		super("Incorrect currency name");
	}
}
