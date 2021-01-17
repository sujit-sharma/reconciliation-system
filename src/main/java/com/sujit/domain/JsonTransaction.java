package com.sujit.domain;

public class JsonTransaction {

    private String date;
    private String reference;
    private Double amount;
    private String currencyCode;
    private String purpose;

    public JsonTransaction(){};

    public JsonTransaction(String date, String reference, Double amount, String currencyCode, String purpose) {
        this.date = date;
        this.reference = reference;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.purpose = purpose;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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
}
