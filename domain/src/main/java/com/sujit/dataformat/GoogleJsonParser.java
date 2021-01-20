package com.sujit.dataformat;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sujit.JsonTransaction;
import com.sujit.Transaction;
import com.sujit.Utils;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class GoogleJsonParser implements Parser {
    private static final DateFormat dateFormatter = Utils.getDateFormatterByParserType(ParserType.JSON);

    @Override
    public List<Transaction> parse(FileReader reader ) {
        JsonTransaction[] jsonTransactions;
        Gson gson = new Gson();
        jsonTransactions = gson.fromJson(new JsonReader(reader), JsonTransaction[].class);

        return jsonObjectToTransactionAdapter(Arrays.asList(jsonTransactions));
    }

    @Override
    public void transfer(List<Transaction> transactions, BufferedWriter writer) {
        throw new UnsupportedOperationException("Method not Implemented Yet");

    }
    @Override
    public void transfer(String line, FileWriter writer) {
        throw new UnsupportedOperationException("Method not Implemented Yet");
    }


    private List<Transaction> jsonObjectToTransactionAdapter(List<JsonTransaction> jsonTransactions) {
        List<Transaction> transactions = new LinkedList<>();
        jsonTransactions.forEach(jsonTransaction -> {
            try{
                Transaction transaction = new Transaction(jsonTransaction.getReference(), jsonTransaction.getAmount()
                        ,jsonTransaction.getCurrencyCode(), jsonTransaction.getPurpose(),dateFormatter.parse(jsonTransaction.getDate()));
                transactions.add(transaction);
            }catch (ParseException parseException){
                Logger.getGlobal().severe("An Exception Occurs" + parseException.getMessage());
            }

        });
        return transactions;
    }
}
