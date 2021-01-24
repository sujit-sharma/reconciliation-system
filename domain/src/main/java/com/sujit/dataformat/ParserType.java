package com.sujit.dataformat;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum ParserType {
  CSV("csv"),
  JSON("json");
  private final String extension;

  ParserType(String extension) {
    this.extension = extension;
  }

  public static Optional<ParserType> getByExtension(String fileFormat) {
    return Arrays.stream(ParserType.values())
        .filter(parserType -> Objects.equals(parserType.getExtension(), fileFormat))
        .findFirst();
  }

  public String getExtension() {
    return extension;
  }
}
