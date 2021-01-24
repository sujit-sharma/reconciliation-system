package com.sujit;

import static org.junit.jupiter.api.Assertions.*;

import com.sujit.dataformat.JsonTransaction;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class JsonTransactionTest {

  @Test
  void WhenInitializedJsonTxnShouldReturnAssignValue() {
    String date = "25/01/2020";
    String reference = "TR-47884222201";
    Double amount = 124.20;
    Currency currency = Currency.getInstance("JOD");
    String purpose = "donation";
    JsonTransaction jsonTransaction =
        new JsonTransaction(date, reference, amount, Currency.getInstance("JOD"), "donation");

    assertEquals(date, jsonTransaction.getDate());
    assertEquals(reference, jsonTransaction.getReference());
    assertEquals(reference, jsonTransaction.getReference());
    assertEquals(amount, jsonTransaction.getAmount());
    assertEquals(currency, jsonTransaction.getCurrencyCode());
    assertEquals(purpose, jsonTransaction.getPurpose());
  }
}
