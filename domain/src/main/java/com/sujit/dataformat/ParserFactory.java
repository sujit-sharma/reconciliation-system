package com.sujit.dataformat;

public class ParserFactory {
  private ParserFactory() {}

  public static Parser getParserByName(ParserType name) {
    if (name.equals(ParserType.CSV)) return new ApacheCsvParser();
    else if (name.equals(ParserType.JSON)) return new GoogleJsonParser();
    throw new IllegalArgumentException("Unsupported file by parsers");
  }
}
