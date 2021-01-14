package com.sujit.domain;

import java.time.LocalDate;
import java.util.Date;

public class Transaction {
    private String transId;
    private String description;
    private Double amount;
    private String currencyCode;
    private String purpose;
    private LocalDate  date;
    private Character transType;

    public Transaction(){}

    public Transaction(String transId, String description, Double amount, String currencyCode, String purpose, LocalDate date, Character transType){
        this.transId = transId;
        this.description = description;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.purpose = purpose;
        this.date = date;
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
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

    @Override
    public String toString() {
        return "Transaction{" +
                "transId='" + transId + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", currencyCode='" + currencyCode + '\'' +
                ", purpose='" + purpose + '\'' +
                ", date=" + date +
                ", transType=" + transType +
                '}';
    }
}
