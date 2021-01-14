package com.sujit.repository.filesystem.CSVImpl;

public enum TransactionHeaders {
    TRANSID("trans unique id"), DESCRIPTION("trans description"), AMOUNT("amount")
    ,CURRENCYCODE("currency"), PURPOSE("purpose"), DATE("date"), TRANSTYPE("trans type");

    public final String level;
   TransactionHeaders(String level){
        this.level = level;
    }
}
