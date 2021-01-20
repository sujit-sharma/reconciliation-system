package com.sujit;

import java.util.Currency;

public class JsonTransaction {

  private final String date;
  private final String reference;
  private final Double amount;
  private final Currency currencyCode;
  private final String purpose;

  public JsonTransaction(
      String date, String reference, Double amount, Currency currencyCode, String purpose) {
    this.date = date;
    this.reference = reference;
    this.amount = amount;
    this.currencyCode = currencyCode;
    this.purpose = purpose;
  }

  public String getDate() {
    return date;
  }

  public String getReference() {
    return reference;
  }

  public Double getAmount() {
    return amount;
  }

  public Currency getCurrencyCode() {
    return currencyCode;
  }

  public String getPurpose() {
    return purpose;
  }
}
