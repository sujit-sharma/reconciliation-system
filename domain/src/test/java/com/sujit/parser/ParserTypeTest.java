package com.sujit.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class ParserTypeTest {

  @Test
  void givenCsvWhenExecutedThenShouldReturnCSV() {
    Optional<ParserType> parserType = ParserType.getByExtension("csv");
    assertEquals(Optional.of(ParserType.CSV), parserType);
  }

  @Test
  void givenJsonWhenExecutedShouldReturnJSON() {
    Optional<ParserType> parserType = ParserType.getByExtension("json");
    assertEquals(Optional.of(ParserType.JSON), parserType);
  }

  @Test
  void givenARandomStringWhenExecutedShouldReturnEmptyOptional() {
    Optional<ParserType> parserType = ParserType.getByExtension("random");
    assertEquals(Optional.empty(), parserType);
  }

  @Test
  void whenCalledGetCSVExtensionShouldReturnStringCsvExtension() {
    String extension = ParserType.CSV.getExtension();
    assertEquals("csv", extension);
  }

  @Test
  void whenCalledGetJSONExtensionShouldReturnStringJsonExtension() {
    String extension = ParserType.JSON.getExtension();
    assertEquals("json", extension);
  }
}
