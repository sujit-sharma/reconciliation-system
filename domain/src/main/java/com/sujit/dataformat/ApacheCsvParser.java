package com.sujit.dataformat;

import com.sujit.Transaction;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class ApacheCsvParser implements Parser {

  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

  @Override
  public List<Transaction> parse(InputStreamReader reader) throws IOException {
    List<Transaction> transactionList = new LinkedList<>();

    Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
    for (CSVRecord attribute : records) {
      Transaction transaction = new Transaction();
      transaction.setTransId(attribute.get(0));
      transaction.setDescription(attribute.get(1));
      transaction.setAmount(Double.parseDouble(attribute.get(2)));

      transaction.setCurrencyCode(Currency.getInstance(attribute.get(3)));
      transaction.setPurpose(attribute.get(4));
      transaction.setDate(LocalDate.parse(attribute.get(5)));
      transaction.setTransType((attribute.get(6).charAt(0)));
      transactionList.add(transaction);
    }

    return transactionList;
  }

  public void transfer(List<Transaction> transactions, BufferedWriter writer) throws IOException {
    CSVPrinter printer =
        CSVFormat.DEFAULT
            .withHeader(
                "trans unique id",
                "trans description",
                "amount",
                "currency",
                "purpose",
                "value date",
                "trans type")
            .print(writer);
    transactions.forEach(
        transaction -> {
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

          } catch (IOException ioException) {
            Logger.getGlobal().severe("An Exception Occurs" + ioException.getMessage());
          }
        });
    printer.flush();
    writer.flush();
  }

  @Override
  public void transfer(String line, FileWriter writer) throws IOException {
    writer.append(String.join(",", line));
    writer.append("\n");
    writer.flush();
  }
}
