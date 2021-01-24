package com.sujit;

import static org.junit.jupiter.api.Assertions.*;

import com.sujit.dataformat.Transaction;
import java.io.*;
import java.time.LocalDate;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
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

    Transaction txn1 =
        new Transaction(
            "TR-47884222201",
            "online transfer",
            Double.parseDouble("140"),
            Currency.getInstance("USD"),
            "donation",
            LocalDate.parse("2020-01-20"),
            'D');
    Transaction txn2 =
        new Transaction(
            "TR-47884222202",
            "atm withdrawal",
            Double.parseDouble("20"),
            Currency.getInstance("JOD"),
            "",
            LocalDate.parse("2020-01-22"),
            'D');
    Transaction txn3 =
        new Transaction(
            "TR-47884222203",
            "counter withdrawal",
            Double.parseDouble("5000"),
            Currency.getInstance("JOD"),
            "",
            LocalDate.parse("2020-01-25"),
            'C');
    Transaction txn4 =
        new Transaction(
            "TR-47884222202",
            "atm withdrawal",
            Double.parseDouble("20"),
            Currency.getInstance("JOD"),
            "",
            LocalDate.parse("2020-03-15"),
            'D');
    Transaction txn5 =
        new Transaction(
            "TR-47884222204",
            "counter withdrawal",
            Double.parseDouble("5000"),
            Currency.getInstance("JOD"),
            "",
            LocalDate.parse("2020-01-25"),
            'C');
    List<Transaction> sourceList = new LinkedList<>();
    List<Transaction> targetList = new LinkedList<>();

    sourceList.add(txn1);
    sourceList.add(txn2);
    sourceList.add(txn3);

    targetList.add(txn1);
    targetList.add(txn4);
    targetList.add(txn5);

    reconciliationProcessor.reconcile(sourceList, targetList);
    String expectedMatching =
        txn1.getTransId()
            + COMMA
            + ReconciliationProcessor.toAmount(txn1.getAmount())
            + COMMA
            + txn1.getCurrencyCode()
            + COMMA
            + txn1.getDate();
    br = new BufferedReader(new FileReader(new File(destinationDir, "MatchingTransactions.csv")));
    br.readLine();
    String actual = br.readLine();
    assertEquals(expectedMatching, actual);
    String expectedMissing1 =
        "SOURCE"
            + COMMA
            + txn3.getTransId()
            + COMMA
            + ReconciliationProcessor.toAmount(txn3.getAmount())
            + COMMA
            + txn3.getCurrencyCode()
            + COMMA
            + txn3.getDate();

    String expectedMissing2 =
        "TARGET"
            + COMMA
            + txn5.getTransId()
            + COMMA
            + ReconciliationProcessor.toAmount(txn5.getAmount())
            + COMMA
            + txn5.getCurrencyCode()
            + COMMA
            + txn5.getDate();
    br = new BufferedReader(new FileReader(new File(destinationDir, "MissingTransactions.csv")));
    br.readLine();
    assertEquals(expectedMissing1, br.readLine());
    assertEquals(expectedMissing2, br.readLine());
  }

  @Test
  void whenExecuteClearDestinationThenShouldClearDestinationDir() {}

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
}
