package com.sujit;

import com.sujit.channel.Channel;
import com.sujit.channel.FileSystemChannel;
import com.sujit.dataformat.Transaction;
import com.sujit.exception.IllegalFileFormatException;
import com.sujit.parser.ApacheCsvParser;
import com.sujit.parser.Parser;
import com.sujit.parser.ParserFactory;
import com.sujit.parser.ParserType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Logger;

public class ReconciliationProcessor {
  public static final String COMMA = ",";
  private final File destinationDir;

  public ReconciliationProcessor(String yourDir) {
    this.destinationDir = new File(yourDir + File.separator + "reconciled-result");
  }

  public File getDestinationDir() {
    return destinationDir;
  }

  public void arrangeDataThenApplyReconciliation(String source, String target) throws IOException {
    clearDestinationDirectory();
    List<Transaction> sourceList = accessData(source);
    List<Transaction> targetList = accessData(target);
    reconcile(sourceList, targetList);
  }

  public void reconcile(List<Transaction> sourceList, List<Transaction> targetList) {
    Map<DaoType, ReconciliationDAO> daoMap = getReconciliationDao();
    for (Iterator<Transaction> sourceItr = sourceList.listIterator(); sourceItr.hasNext(); ) {
      Transaction sourceTrans = sourceItr.next();

      for (Iterator<Transaction> targetItr = targetList.listIterator(); targetItr.hasNext(); ) {
        Transaction targetTrans = targetItr.next();
        if (sourceTrans.getTransId().equals(targetTrans.getTransId())) {
          if (sourceTrans.isMatched(targetTrans)) {
            daoMap.get(DaoType.MATCHING).saveRow(csvLine(sourceTrans, ""));
            sourceItr.remove();
          } else {
            daoMap.get(DaoType.MISMATCHING).saveRow(csvLine(sourceTrans, "SOURCE"));
            sourceItr.remove();
            daoMap.get(DaoType.MISMATCHING).saveRow(csvLine(targetTrans, "TARGET"));
          }
          targetItr.remove();
        }
      }
    }
    sourceList.forEach(
        transaction -> daoMap.get(DaoType.MISSING).saveRow(csvLine(transaction, "SOURCE")));
    targetList.forEach(
        transaction -> daoMap.get(DaoType.MISSING).saveRow(csvLine(transaction, "TARGET")));
    Logger.getGlobal().info("Result files are available in directory " + destinationDir);
  }

  private Map<DaoType, ReconciliationDAO> getReconciliationDao() {
    Map<DaoType, ReconciliationDAO> daoMap = new LinkedHashMap<>(3);
    ReconciliationDAO matchingDao = createReconciliationDao("MatchingTransactions.csv");
    ReconciliationDAO mismatchingDao = createReconciliationDao("MismatchingTransactions.csv");
    ReconciliationDAO missingDao = createReconciliationDao("MissingTransactions.csv");
    matchingDao.saveRow("transaction id,amount,currency code,value date");
    mismatchingDao.saveRow("found in file,transaction id,amount,currency code,value date");
    missingDao.saveRow("found in file,transaction id,amount,currency code,value date");

    daoMap.put(DaoType.MATCHING, matchingDao);
    daoMap.put(DaoType.MISMATCHING, mismatchingDao);
    daoMap.put(DaoType.MISSING, missingDao);
    return daoMap;
  }

  private ReconciliationDAO createReconciliationDao(String fileName) {
    return new ReconciliationDAOImpl(
        new FileSystemChannel(new ApacheCsvParser(), new File(destinationDir, fileName)));
  }

  public void clearDestinationDirectory() throws IOException {
    if (destinationDir.exists()) {
      for (File file : Objects.requireNonNull(destinationDir.listFiles())) {
        Files.delete(file.toPath());
      }
      Files.delete(destinationDir.toPath());
    }
    if (!destinationDir.mkdirs()) throw new IllegalArgumentException("Invalid directory");
  }

  private List<Transaction> accessData(String filePath) {
    String[] parts = filePath.split("\\.");
    String fileFormat = parts[parts.length - 1];
    Optional<ParserType> parserType = ParserType.getByExtension(fileFormat);
    if (parserType.isEmpty()) {
      throw new IllegalFileFormatException("File format not supported");
    }

    Parser parser = ParserFactory.getParserByName(parserType.get());

    Channel sourceChannel = new FileSystemChannel(parser, new File(filePath));
    ReconciliationDAO sourceDao = new ReconciliationDAOImpl(sourceChannel);
    return sourceDao.findAll();
  }

  private String csvLine(Transaction transaction, String source) {
    String txnRow =
        transaction.getTransId()
            + COMMA
            + toAmount(transaction.getAmount())
            + COMMA
            + transaction.getCurrencyCode()
            + COMMA
            + transaction.getDate();
    return source != null && !source.trim().isEmpty() ? (source + COMMA + txnRow) : txnRow;
  }

  public static String toAmount(Double amount) {
    DecimalFormat format = (DecimalFormat) NumberFormat.getCurrencyInstance();
    String pattern = format.toPattern();
    String newPattern = pattern.replace("\u00A4", "").replace(",", "").trim();
    DecimalFormat decimalFormat = new DecimalFormat(newPattern);
    return decimalFormat.format(amount);
  }
}
