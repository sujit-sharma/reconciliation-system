package com.sujit.domain.dataformat;

import com.sujit.domain.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ApacheCsvParser implements Parser {

    @Override
    public List<Transaction> parse(FileReader reader) throws IOException {
        List<Transaction> transactionList = new ArrayList<>();
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(reader);
        for (CSVRecord attribute : records ) {
            Transaction transaction = new Transaction();
            transaction.setTransId(attribute.get("trans unique id"));
            transaction.setDescription(attribute.get("trans description"));
            transaction.setAmount(Double.parseDouble(attribute.get("amount")));
            transaction.setCurrencyCode(attribute.get("currency"));
            transaction.setPurpose(attribute.get("purpose"));
            transaction.setDate(attribute.get("value date"));
            transaction.setTransType((attribute.get("trans type").charAt(0)));

            transactionList.add(transaction);
        }

        return transactionList;
    }

    @Override
    public void transfer(List<Transaction> transactions, BufferedWriter writer) throws IOException {
        CSVPrinter printer = CSVFormat.DEFAULT.withHeader("trans unique id","trans description","amount"
                ,"currency","purpose","value date","trans type").print(writer);
        transactions.forEach(transaction -> {
            try {
                printer.print(transaction.getTransId());
                printer.print(transaction.getDescription());
                printer.print(transaction.getAmount());
                printer.print(transaction.getCurrencyCode());
                printer.print(transaction.getPurpose());
                printer.print(transaction.getDate());
                printer.print(transaction.getTransType());
                printer.println();
                printer.printRecords();

            }
            catch (IOException ioException){
                ioException.printStackTrace();
            }

        });
        printer.flush();
        writer.flush();

    }
}
