package com.sujit.dataformat;

import static org.junit.jupiter.api.Assertions.*;

import com.sujit.Transaction;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApacheCsvParserTest {

  private ApacheCsvParser parser;

  @BeforeEach
  void setUp() {
    parser = new ApacheCsvParser();
  }

  @Test
  void givenFileWhenParseThenShouldReturnTxnList() throws IOException {
    InputStream reader = readAsStream("input.csv");

    List<Transaction> result = parser.parse(new InputStreamReader(reader));

    assertEquals(1, result.size());

    Transaction firstTxn = result.get(0);

    assertEquals("1", firstTxn.getTransId());
    assertEquals("Credit Line", firstTxn.getDescription());
    assertEquals(Double.valueOf("100"), firstTxn.getAmount());
    assertEquals(Currency.getInstance("USD"), firstTxn.getCurrencyCode());
    assertEquals("Family Maintenance", firstTxn.getPurpose());
    assertEquals(LocalDate.parse("2010-10-20"), firstTxn.getDate());
    assertEquals('D', firstTxn.getTransType());
  }

  private InputStream readAsStream(String name) {
    return getClass().getClassLoader().getResourceAsStream(name);
  }
}
