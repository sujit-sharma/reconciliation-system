package com.sujit.dataformat;

import static org.junit.jupiter.api.Assertions.*;

import com.sujit.Transaction;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApacheCsvParserTest {

  private ApacheCsvParser parser;
  final String COMMA = ",";

  @BeforeEach
  void setUp() {
    parser = new ApacheCsvParser();
  }

  @Test
  void givenFileWhenParseThenShouldReturnTxnList() throws IOException {
    InputStream reader = readAsStream("input.csv");

    List<Transaction> result = parser.parse(new InputStreamReader(reader));

    assertEquals(6, result.size());

    Transaction firstTxn = result.get(0);

    assertEquals("TR-47884222201", firstTxn.getTransId());
    assertEquals("online transfer", firstTxn.getDescription());
    assertEquals(Double.valueOf("140"), firstTxn.getAmount());
    assertEquals(Currency.getInstance("USD"), firstTxn.getCurrencyCode());
    assertEquals("donation", firstTxn.getPurpose());
    assertEquals(LocalDate.parse("2020-01-20"), firstTxn.getDate());
    assertEquals('D', firstTxn.getTransType());

    Transaction lastTxn = result.get(result.size() - 1);

    assertEquals("TR-47884222206", lastTxn.getTransId());
    assertEquals("atm withdrawal", lastTxn.getDescription());
    assertEquals(Double.valueOf("500"), lastTxn.getAmount());
    assertEquals(Currency.getInstance("USD"), lastTxn.getCurrencyCode());
    assertEquals("", lastTxn.getPurpose());
    assertEquals(LocalDate.parse("2020-02-10"), lastTxn.getDate());
    assertEquals('D', lastTxn.getTransType());
  }

  private InputStream readAsStream(String name) {
    return getClass().getClassLoader().getResourceAsStream(name);
  }

  @Test
  void givenARowAndFileWhenTransferThenShouldWriteTxnInFile() throws IOException {
    String fileName = "line_output.csv";
    Path filePath = Paths.get(this.getClass().getResource("/" + fileName).getPath());
    FileWriter writer = new FileWriter(filePath.toFile());
    final String COMMA = ",";
    Transaction expected =
        new Transaction(
            "TR-47884222201",
            "online transfer",
            Double.parseDouble("140"),
            Currency.getInstance("USD"),
            "donation",
            LocalDate.parse("2020-01-20"),
            'D');
    String row =
        expected.getTransId()
            + COMMA
            + expected.getAmount()
            + COMMA
            + expected.getCurrencyCode()
            + COMMA
            + expected.getDate();

    parser.transfer(row, writer);
    InputStream reader = readAsStream(fileName);
    String actual = new String(reader.readAllBytes());

    assertEquals(row + "\n", actual);
  }

  @Test
  void givenMultipleRowsWhenAFileWhenTransferThenShouldWriteAllTxnInFile() throws IOException {
    String fileName = "list_output.csv";
    Path filePath = Paths.get(this.getClass().getResource("/" + fileName).getPath());
    FileWriter writer = new FileWriter(filePath.toFile());
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
    String row1 =
        txn1.getTransId()
            + COMMA
            + txn1.getAmount()
            + COMMA
            + txn1.getCurrencyCode()
            + COMMA
            + txn1.getDate();

    String row2 =
        txn2.getTransId()
            + COMMA
            + txn2.getAmount()
            + COMMA
            + txn2.getCurrencyCode()
            + COMMA
            + txn2.getDate();
    String row3 =
        txn3.getTransId()
            + COMMA
            + txn3.getAmount()
            + COMMA
            + txn3.getCurrencyCode()
            + COMMA
            + txn3.getDate();

    parser.transfer(row1, writer);
    parser.transfer(row2, writer);
    parser.transfer(row3, writer);

    InputStream reader = readAsStream(fileName);
    String actual = new String(reader.readAllBytes());
    String expected = row1 + "\n" + row2 + "\n" + row3 + "\n";

    assertEquals(expected, actual);
  }

  @Test
  void givenTxnListAndFileWhenTransferShouldWriteAllGivenTxnInFile() throws IOException {
    String fileName = "list_output.csv";
    Path filePath = Paths.get(this.getClass().getResource(File.separator + fileName).getPath());
    BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));

    List<Transaction> txnList = new LinkedList<>();
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
    txnList.add(txn1);
    txnList.add(txn2);
    txnList.add(txn3);
    parser.transfer(txnList, writer);
    InputStream reader = readAsStream(fileName);
    String actual = new String(reader.readAllBytes());

    StringBuilder expected =
        new StringBuilder(
            "trans unique id,trans description,amount,currency,purpose,value date,trans type\r\n");

    for (Transaction txn : txnList) {
      expected
          .append(txn.getTransId())
          .append(COMMA)
          .append(txn.getDescription())
          .append(COMMA)
          .append(txn.getAmount())
          .append(COMMA)
          .append(txn.getCurrencyCode())
          .append(COMMA)
          .append(txn.getPurpose())
          .append(COMMA)
          .append(txn.getDate())
          .append(COMMA)
          .append(txn.getTransType())
          .append("\r\n");
    }

    assertEquals(expected.toString(), actual);
  }
}
