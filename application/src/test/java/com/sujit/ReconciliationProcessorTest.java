package com.sujit;

import static org.junit.jupiter.api.Assertions.*;

import com.sujit.dataformat.Transaction;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReconciliationProcessorTest {
  private ReconciliationProcessor reconciliationProcessor;
  final String COMMA = ",";
  private File destinationDir;

  @BeforeEach
  void init() throws IOException {
    String[] parts =
        getClass().getClassLoader().getResource("file1.csv").getFile().split("/file1.csv");
    String pathname = parts[0] + File.separator + "output";
    reconciliationProcessor = new ReconciliationProcessor(pathname);
    destinationDir = reconciliationProcessor.getDestinationDir();
    reconciliationProcessor.clearDestinationDirectory();
  }

  @Test
  void givenSourceAndTargetFileWhenExecuteThenShouldCallReconciliationProcessWithoutAnyException() {
    try {
      String source =
          "."
              + File.separator
              + "src"
              + File.separator
              + "test"
              + File.separator
              + "resources"
              + File.separator
              + "file1.csv";
      String target =
          "."
              + File.separator
              + "src"
              + File.separator
              + "test"
              + File.separator
              + "resources"
              + File.separator
              + "file2.json";

      reconciliationProcessor.arrangeDataThenApplyReconciliation(source, target);
    } catch (Exception e) {
      fail("Should run without any exception", e);
    }
  }



  @Test
  void givenSourceAndTargetTxnListWhenReconcileThenShouldDetectMissingTxnAndSeparateThem()
      throws IOException {
    BufferedReader br;
    List<Transaction> txn = createTransactions();
    List<Transaction> sourceList = new LinkedList<>();
    List<Transaction> targetList = new LinkedList<>();
    sourceList.add(txn.get(0));
    sourceList.add(txn.get(1));
    sourceList.add(txn.get(2));

    targetList.add(txn.get(0));
    targetList.add(txn.get(3));
    targetList.add(txn.get(4));
    reconciliationProcessor.reconcile(sourceList, targetList);
    br = new BufferedReader(new FileReader(new File(destinationDir, "MatchingTransactions.csv")));
    br.readLine();
    String actual = br.readLine();
    assertEquals(line(txn.get(0), ""), actual);

    br = new BufferedReader(new FileReader(new File(destinationDir, "MissingTransactions.csv")));
    br.readLine();
    assertEquals(line(txn.get(2), "SOURCE"), br.readLine());
    assertEquals(line(txn.get(4), "TARGET"), br.readLine());
  }

  @Test
  void whenExecuteShouldReturnMapOfDaoTypeAndReconciliationDaoWithSize3()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    Method createDao = reconciliationProcessor.getClass().getDeclaredMethod("getReconciliationDao");
    createDao.setAccessible(true);
    Object object = createDao.invoke(reconciliationProcessor);
    Map<DaoType, ReconciliationDAO> DaoMap = (Map) object;
    assertEquals(3, DaoMap.size());
  }

  @Test
  void whenExecuteClearDestinationThenShouldDeleteAllContentsOfDir() throws IOException {
    String destinationDir = "." + File.separator + "test" + File.separator + "resources";
    ReconciliationProcessor recProcessor = new ReconciliationProcessor(destinationDir);
    recProcessor.clearDestinationDirectory();
    assertEquals(0, recProcessor.getDestinationDir().list().length);
    assertTrue(recProcessor.getDestinationDir().exists());
  }

  @Test
  void givenDecimalWhenExecuteThenShouldReturnStringValueWithDefaultPrecision() {
    assertEquals("20.12", ReconciliationProcessor.toAmount(Double.parseDouble("20.1197")));
    assertEquals("120.45", ReconciliationProcessor.toAmount(Double.parseDouble("120.45")));
    assertEquals("0.00", ReconciliationProcessor.toAmount(Double.parseDouble("0")));
  }

  @Test
  void givenDecimalGreaterThen1000WhenExecutedShouldReturnStringWithDefaultPrecision() {
    assertEquals("4000.00", ReconciliationProcessor.toAmount(Double.parseDouble("4000.0000")));
    assertEquals("1000.00", ReconciliationProcessor.toAmount(Double.parseDouble("1000")));
    assertEquals(
        "1234226.13", ReconciliationProcessor.toAmount(Double.parseDouble("1234226.13264")));
  }

  private String line(Transaction transaction, String source) {
    String txnRow =
        transaction.getTransId()
            + COMMA
            + ReconciliationProcessor.toAmount(transaction.getAmount())
            + COMMA
            + transaction.getCurrencyCode()
            + COMMA
            + transaction.getDate();
    return source != null && !source.trim().isEmpty() ? (source + COMMA + txnRow) : txnRow;
  }
  private List<Transaction> createTransactions() {
    List<Transaction> transactionList = new LinkedList<>();
    transactionList.add(
            new Transaction(
                    "TR-47884222201",
                    "online transfer",
                    Double.parseDouble("140"),
                    Currency.getInstance("USD"),
                    "donation",
                    LocalDate.parse("2020-01-20"),
                    'D'));
    transactionList.add(
            new Transaction(
                    "TR-47884222202",
                    "atm withdrawal",
                    Double.parseDouble("20"),
                    Currency.getInstance("JOD"),
                    "",
                    LocalDate.parse("2020-01-22"),
                    'D'));
    transactionList.add(
            new Transaction(
                    "TR-47884222203",
                    "counter withdrawal",
                    Double.parseDouble("5000"),
                    Currency.getInstance("JOD"),
                    "",
                    LocalDate.parse("2020-01-25"),
                    'C'));
    transactionList.add(
            new Transaction(
                    "TR-47884222202",
                    "atm withdrawal",
                    Double.parseDouble("20"),
                    Currency.getInstance("JOD"),
                    "",
                    LocalDate.parse("2020-03-15"),
                    'D'));
    transactionList.add(
            new Transaction(
                    "TR-47884222204",
                    "counter withdrawal",
                    Double.parseDouble("5000"),
                    Currency.getInstance("JOD"),
                    "",
                    LocalDate.parse("2020-01-25"),
                    'C'));
    return transactionList;
  }
}
