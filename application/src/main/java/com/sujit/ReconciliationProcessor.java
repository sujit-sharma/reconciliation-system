package com.sujit;

import com.sujit.channel.Channel;
import com.sujit.channel.FileSystemChannel;
import com.sujit.dataformat.ApacheCsvParser;
import com.sujit.dataformat.GoogleJsonParser;
import com.sujit.dataformat.Parser;
import com.sujit.dataformat.ParserType;
import com.sujit.exception.IllegalFileFormatException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Logger;

public class ReconciliationProcessor {
  final File destinationDir = new File("/home/sujit/clusus/reconciled-result");

  public void arrangeDataThenApplyReconciliation(String source, String target) throws IOException {
    if (destinationDir.exists() && destinationDir.length() > 0) {
      for (File file : Objects.requireNonNull(destinationDir.listFiles())) {
        if (destinationDir.length() > 0) Files.delete(file.toPath());
      }
    }
    if (destinationDir.mkdir()) throw new IllegalArgumentException("Invalid directory");

    List<Transaction> sourceList = accessData(source);
    List<Transaction> targetList = accessData(target);
    reconcile(sourceList, targetList);
  }

  public void reconcile(List<Transaction> sourceList, List<Transaction> targetList) {
    final String COMMA = ",";
    // arranging system to write in different files
    ReconciliationDAO matchingDao =
        new ReconciliationDAOImpl(
            new FileSystemChannel(
                new ApacheCsvParser(), new File(destinationDir + "/MatchingTransactions.csv")));
    matchingDao.saveRow("transaction id,amount,currency code,value date");
    ReconciliationDAO mismatchingDao =
        new ReconciliationDAOImpl(
            new FileSystemChannel(
                new ApacheCsvParser(), new File(destinationDir + "/MismatchingTransactions.csv")));
    mismatchingDao.saveRow("found in file,transaction id,amount,currency code,value date");
    ReconciliationDAO missingDao =
        new ReconciliationDAOImpl(
            new FileSystemChannel(
                new ApacheCsvParser(), new File(destinationDir + "/MissingTransactions.csv")));
    missingDao.saveRow("found in file,transaction id,amount,currency code,value date");

    for (Iterator<Transaction> sourceItr = sourceList.listIterator(); sourceItr.hasNext(); ) {
      Transaction sourceTrans = sourceItr.next();

      for (Iterator<Transaction> targetItr = targetList.listIterator(); targetItr.hasNext(); ) {
        Transaction targetTrans = targetItr.next();
        if (sourceTrans.getTransId().equals(targetTrans.getTransId())) {
          if (sourceTrans.getAmount().equals(targetTrans.getAmount())
              && sourceTrans.getCurrencyCode().equals(targetTrans.getCurrencyCode())
              && sourceTrans.getDate().equals(targetTrans.getDate())) {
            String row =
                sourceTrans.getTransId()
                    + COMMA
                    + sourceTrans.getAmount()
                    + COMMA
                    + sourceTrans.getCurrencyCode()
                    + COMMA
                    + sourceTrans.getDate();
            matchingDao.saveRow(row);
            sourceItr.remove();
          } else {
            String row1 =
                "SOURCE"
                    + COMMA
                    + sourceTrans.getTransId()
                    + COMMA
                    + NumberFormat.getCurrencyInstance().format(sourceTrans.getAmount())
                    + COMMA
                    + sourceTrans.getCurrencyCode()
                    + COMMA
                    + sourceTrans.getDate();
            mismatchingDao.saveRow(row1);
            sourceItr.remove();
            String row2 =
                "TARGET"
                    + COMMA
                    + targetTrans.getTransId()
                    + COMMA
                    + NumberFormat.getCurrencyInstance().format(targetTrans.getAmount())
                    + COMMA
                    + targetTrans.getCurrencyCode()
                    + COMMA
                    + targetTrans.getDate();
            mismatchingDao.saveRow(row2);
          }
          targetItr.remove();
        }
      }
    }
    sourceList.forEach(
        transaction -> {
          String row1 =
              "SOURCE"
                  + COMMA
                  + transaction.getTransId()
                  + COMMA
                  + transaction.getAmount()
                  + COMMA
                  + transaction.getCurrencyCode()
                  + COMMA
                  + transaction.getDate();
          missingDao.saveRow(row1);
        });
    targetList.forEach(
        transaction -> {
          String row1 =
              "TARGET"
                  + COMMA
                  + transaction.getTransId()
                  + COMMA
                  + transaction.getAmount()
                  + COMMA
                  + transaction.getCurrencyCode()
                  + COMMA
                  + transaction.getDate();
          missingDao.saveRow(row1);
        });
    Logger.getGlobal().info("Result files are available in directory " + destinationDir);
  }

  private List<Transaction> accessData(String filePath) {
    String fileFormat = filePath.substring(filePath.indexOf('.') + 1);
    if (fileFormat
        .toLowerCase(Locale.ROOT)
        .contains(Arrays.toString(ParserType.values()).toLowerCase(Locale.ROOT))) {
      throw new IllegalFileFormatException("File format not supported");
    }
    Parser parser;
    parser = fileFormat.equalsIgnoreCase("csv") ? new ApacheCsvParser() : new GoogleJsonParser();
    Channel sourceChannel = new FileSystemChannel(parser, new File(filePath));
    ReconciliationDAO sourceDao = new ReconciliationDAOImpl(sourceChannel);
    return sourceDao.findAll();
  }
}
