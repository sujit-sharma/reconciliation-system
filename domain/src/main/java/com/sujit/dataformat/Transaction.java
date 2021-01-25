package com.sujit.dataformat;

import java.time.LocalDate;
import java.util.Currency;

public class Transaction {
  private String transId;
  private String description;
  private Double amount;
  private Currency currencyCode;
  private String purpose;
  private LocalDate date;
  private Character transType;

  public Transaction() {}

  public Transaction(
      String transId, Double amount, Currency currencyCode, String purpose, LocalDate date) {
    this.transId = transId;
    this.amount = amount;
    this.currencyCode = currencyCode;
    this.purpose = purpose;
    this.date = date;
  }

  public Transaction(
      String transId,
      String description,
      Double amount,
      Currency currencyCode,
      String purpose,
      LocalDate date,
      Character transType) {
    this(transId, amount, currencyCode, purpose, date);
    this.description = description;
    this.transType = transType;
  }

  public String getTransId() {
    return transId;
  }

  public void setTransId(String transId) {
    this.transId = transId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Currency getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(Currency currencyCode) {
    this.currencyCode = currencyCode;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Character getTransType() {
    return transType;
  }

  public void setTransType(Character transType) {
    this.transType = transType;
  }

  public boolean isMatched(Transaction targetTrans) {
    return this.getAmount().equals(targetTrans.getAmount())
        && this.getCurrencyCode().equals(targetTrans.getCurrencyCode())
        && this.getDate().equals(targetTrans.getDate());
  }
}
