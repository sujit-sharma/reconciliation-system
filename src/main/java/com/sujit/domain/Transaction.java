package com.sujit.domain;

import java.util.Date;

public class Transaction {
    private String transId;
    private Double amount;
    private String currencyCode;
    private String purpose;
    private Date  date;
    private String transType;

    public Transaction(){}

    public Transaction(String transId, Double amount, String currencyCode, String purpose, String transType, Date date){
        this.transId = transId;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.purpose = purpose;
        this.transType = transType;
        this.date = date;

    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
}
