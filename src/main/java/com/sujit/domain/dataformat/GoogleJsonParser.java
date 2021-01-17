package com.sujit.domain.dataformat;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sujit.domain.JsonTransaction;
import com.sujit.domain.Transaction;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleJsonParser implements Parser {
    @Override
    public List<Transaction> parse(FileReader reader ) {
        JsonTransaction[] jsonTransactions = null;
        Gson gson = new Gson();
        jsonTransactions = gson.fromJson(new JsonReader(reader), Transaction[].class);

        return jsonObjectToTransactionAdapter(Arrays.asList(jsonTransactions));
    }

    @Override
    public void transfer(List<Transaction> transactions, BufferedWriter writer) {

    }


    private List<Transaction> jsonObjectToTransactionAdapter(List<JsonTransaction> jsonTransactions) {
        List<Transaction> transactions = new ArrayList<>();
        jsonTransactions.forEach(jsonTransaction -> {
            Transaction transaction = new Transaction(jsonTransaction.getReference(), jsonTransaction.getAmount()
                    ,jsonTransaction.getCurrencyCode(), jsonTransaction.getPurpose(),jsonTransaction.getDate());
            transactions.add(transaction);
        });
        return transactions;
    }
}
