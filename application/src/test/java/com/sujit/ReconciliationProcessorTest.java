package com.sujit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReconciliationProcessorTest {
  private ReconciliationProcessor reconciliationProcessor;
  private File destinationDir;
  final String COMMA = ",";

  @BeforeEach
  void init() {
    reconciliationProcessor = new ReconciliationProcessor();
  }

  @AfterEach
  void destroy() throws IOException {
    if (destinationDir != null) {
      if (destinationDir.exists() && destinationDir.length() > 0) {
        for (File file : Objects.requireNonNull(destinationDir.listFiles())) {
          if (destinationDir.length() > 0) Files.delete(file.toPath());
        }
      }
    }
  }

  @Test
  void givenSourceAndTargetFileWhenExecuteThenShouldCallReconciliationProcess() throws IOException {
    String source =
        System.getProperty("user.home") + File.separator + "clusus" + File.separator + "file1.csv";
    String target =
        System.getProperty("user.home") + File.separator + "clusus" + File.separator + "file2.json";

    reconciliationProcessor.arrangeDataThenApplyReconciliation(source, target);
  }

  @Test
  void givenSourceAndTargetTxnListWhenReconcileThenShouldDetectMissingTxnAndSeparateThem()
      throws IOException {
    destinationDir =
        new File(
            "."
                + File.separator
                + "src"
                + File.separator
                + "test"
                + File.separator
                + "resources"
                + File.separator
                + "output");
    boolean isCreated = destinationDir.mkdir();
    if (!isCreated) {
      Logger.getGlobal().info("Directory already created");
    }
    reconciliationProcessor.setDestinationDir(destinationDir);
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
            "atm withdrwal",
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
            "atm withdrwal",
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
            + txn1.getAmount()
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
            + txn3.getAmount()
            + COMMA
            + txn3.getCurrencyCode()
            + COMMA
            + txn3.getDate();

    String expectedMissing2 =
        "TARGET"
            + COMMA
            + txn5.getTransId()
            + COMMA
            + txn5.getAmount()
            + COMMA
            + txn5.getCurrencyCode()
            + COMMA
            + txn5.getDate();
    br = new BufferedReader(new FileReader(new File(destinationDir, "MissingTransactions.csv")));
    br.readLine();
    assertEquals(expectedMissing1, br.readLine());
    assertEquals(expectedMissing2, br.readLine());
  }
}
