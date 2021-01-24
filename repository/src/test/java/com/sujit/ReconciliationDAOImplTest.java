package com.sujit;

import static org.junit.jupiter.api.Assertions.*;

import com.sujit.channel.Channel;
import com.sujit.channel.FileSystemChannel;
import com.sujit.dataformat.ApacheCsvParser;
import com.sujit.dataformat.GoogleJsonParser;
import com.sujit.dataformat.Parser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReconciliationDAOImplTest {

  @Test
  void whenExecuteFindAllWithCsvFileAndCsvParserThenShouldReturnListOfTransactions() {
    Parser parser = new ApacheCsvParser();
    File file =
        new File(
            "."
                + File.separator
                + "src"
                + File.separator
                + "test"
                + File.separator
                + "resources"
                + File.separator
                + "input.csv");
    Channel channel = new FileSystemChannel(parser, file);

    ReconciliationDAOImpl dao = new ReconciliationDAOImpl(channel);
    List<Transaction> result = dao.findAll();

    assertEquals(6, result.size());
  }

  @Test
  void whenExecuteFindAllWithJsonFileAndJsonParserThenShouldReturnListOfTransactions() {
    Parser parser = new GoogleJsonParser();
    File file =
        new File(
            "."
                + File.separator
                + "src"
                + File.separator
                + "test"
                + File.separator
                + "resources"
                + File.separator
                + "input.json");
    Channel channel = new FileSystemChannel(parser, file);

    ReconciliationDAOImpl dao = new ReconciliationDAOImpl(channel);
    List<Transaction> result = dao.findAll();

    assertEquals(7, result.size());
  }

  @Test
  void givenARowWhenExecuteSaveRowThenShouldPersistRow() throws IOException {
    Parser parser = new ApacheCsvParser();
    File file =
        new File(
            "."
                + File.separator
                + "src"
                + File.separator
                + "test"
                + File.separator
                + "resources"
                + File.separator
                + "fileChannelOutput.csv");
    Channel channel = new FileSystemChannel(parser, file);
    ReconciliationDAOImpl dao = new ReconciliationDAOImpl(channel);
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
    dao.saveRow(row);
    byte[] actual = Files.readAllBytes(file.toPath());

    assertEquals(row + "\n", new String(actual));

    file.delete();
  }
}
