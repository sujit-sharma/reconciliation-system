package com.sujit.dataformat;

import com.sujit.Transaction;
import com.sujit.Utils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class ApacheCsvParser implements Parser {

    private static final DateFormat dateFormatter = Utils.getDateFormatterByParserType(ParserType.CSV);

    @Override
    public List<Transaction> parse(FileReader reader) throws IOException {
        List<Transaction> transactionList = new LinkedList<>();

        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(reader);
        for (CSVRecord attribute : records ) {
            Transaction transaction = new Transaction();
            transaction.setTransId(attribute.get("trans unique id"));
            transaction.setDescription(attribute.get("trans description"));
            transaction.setAmount(Double.parseDouble(attribute.get("amount")));

            transaction.setCurrencyCode(Currency.getInstance(attribute.get("currency")));
            transaction.setPurpose(attribute.get("purpose"));
            try{
                transaction.setDate(dateFormatter.parse(attribute.get("value date")));
            }catch (ParseException parseException){
                Logger.getGlobal().severe("An Exception Occurs" + parseException.getMessage());
            }
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
                printer.print(dateFormatter.format(transaction.getDate()));
                printer.print(transaction.getTransType());
                printer.println();
                printer.printRecords();

            }
            catch (IOException ioException){
                Logger.getGlobal().severe("An Exception Occurs" + ioException.getMessage());
            }

        });
        printer.flush();
        writer.flush();

    }

    @Override
    public void transfer(String line, FileWriter writer) throws IOException {
        writer.append(String.join(",", line ));
        writer.append("\n");
        writer.flush();
    }
}
