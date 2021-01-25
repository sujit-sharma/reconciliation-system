package com.sujit.dataformat;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionTest {
  Transaction sourceTransaction;
  Transaction targetTransaction;

  @BeforeEach
  void setUp() {
    sourceTransaction =
        new Transaction(
            "TR-47884222201",
            "online transfer",
            Double.parseDouble("140"),
            Currency.getInstance("USD"),
            "donation",
            LocalDate.parse("2020-01-20"),
            'D');
    targetTransaction =
        new Transaction(
            "TR-47884222201",
            "online transfer",
            Double.parseDouble("140"),
            Currency.getInstance("USD"),
            "donation",
            LocalDate.parse("2020-01-20"),
            'D');
  }

  @Test
  void givenMatchedTransactionWhenExecutedThenShouldReturnTrue() {
    assertTrue(sourceTransaction.isMatched(targetTransaction));
  }

  @Test
  void givenAmountDifferentWhenExecutedThenShouldReturnFalse() {
    targetTransaction.setAmount(Double.parseDouble("150.12"));
    assertFalse(sourceTransaction.isMatched(targetTransaction));
  }

  @Test
  void givenDifferentCurrencyCodeWhenExecutedThenShouldReturnFalse() {
    targetTransaction.setCurrencyCode(Currency.getInstance("JOD"));
    assertFalse(sourceTransaction.isMatched(targetTransaction));
  }

  @Test
  void givenDifferentDateWhenExecutedThenShouldReturnFalse() {
    targetTransaction.setDate(LocalDate.parse("2020-05-17"));
    assertFalse(sourceTransaction.isMatched(targetTransaction));
  }
}
