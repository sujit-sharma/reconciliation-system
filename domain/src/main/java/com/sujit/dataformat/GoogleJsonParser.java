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

public class GoogleJsonParser implements Parser {
    private static final DateFormat dateFormatter = Utils.getDateFormatterByParserType(ParserType.JSON);

    @Override
    public List<Transaction> parse(FileReader reader ) {
        JsonTransaction[] jsonTransactions = null;
        Gson gson = new Gson();
        jsonTransactions = gson.fromJson(new JsonReader(reader), JsonTransaction[].class);

        return jsonObjectToTransactionAdapter(Arrays.asList(jsonTransactions));
    }

    @Override
    public void transfer(List<Transaction> transactions, BufferedWriter writer) {

    }
    @Override
    public void transfer(String line, FileWriter writer) throws IOException {

    }


    private List<Transaction> jsonObjectToTransactionAdapter(List<JsonTransaction> jsonTransactions) {
        List<Transaction> transactions = new LinkedList<>();
        jsonTransactions.forEach(jsonTransaction -> {
            try{
                Transaction transaction = new Transaction(jsonTransaction.getReference(), jsonTransaction.getAmount()
                        ,jsonTransaction.getCurrencyCode(), jsonTransaction.getPurpose(),dateFormatter.parse(jsonTransaction.getDate()));
                transactions.add(transaction);
            }catch (ParseException parseException){
                parseException.printStackTrace();
            }

        });
        return transactions;
    }
}