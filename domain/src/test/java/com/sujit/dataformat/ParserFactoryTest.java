package com.sujit.dataformat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ParserFactoryTest {

  @Test
  void givenCsvAsInput_WhenCalledGetParser_ThenShouldReturnApacheCsvParser() {
    Parser parser = ParserFactory.getParserByName(ParserType.CSV);
    assertTrue(parser instanceof ApacheCsvParser);
  }

  @Test
  void givenJsonAsInput_WhenCalledGetParser_ThenShouldReturnGoogleJsonParser() {
    Parser parser = ParserFactory.getParserByName(ParserType.JSON);
    assertTrue(parser instanceof GoogleJsonParser);
  }
}
