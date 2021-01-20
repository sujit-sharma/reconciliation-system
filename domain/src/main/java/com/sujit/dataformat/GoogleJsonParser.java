package com.sujit.dataformat;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sujit.JsonTransaction;
import com.sujit.Transaction;
import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GoogleJsonParser implements Parser {

  @Override
  public List<Transaction> parse(InputStreamReader reader) {
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
    jsonTransactions.forEach(
        jsonTransaction -> {
          Transaction transaction =
              new Transaction(
                  jsonTransaction.getReference(),
                  jsonTransaction.getAmount(),
                  jsonTransaction.getCurrencyCode(),
                  jsonTransaction.getPurpose(),
                  LocalDate.parse(jsonTransaction.getDate()));
          transactions.add(transaction);
        });
    return transactions;
  }
}
