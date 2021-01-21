package com.sujit.dataformat;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

class ParserFactoryTest {

  @Test
  public void givenCsvAsInput_WhenCalledGetParser_ThenShouldReturnApacheCsvParser() {
    Parser parser = ParserFactory.getParserByName(ParserType.CSV);
    assertTrue(parser instanceof ApacheCsvParser);
  }

  @Test
  public void givenJsonAsInput_WhenCalledGetParser_ThenShouldReturnGoogleJsonParser() {
    Parser parser = ParserFactory.getParserByName(ParserType.JSON);
    assertTrue(parser instanceof GoogleJsonParser);
  }

  @Test
  public void givenNullValueAsInput_WhenCalledGetParser_ThenShouldThrowNoSuchParserException() {
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> ParserFactory.getParserByName(ParserType.valueOf("something")));
    Logger.getGlobal().severe("An Exception Occurs" + exception.getMessage());
  }
}
