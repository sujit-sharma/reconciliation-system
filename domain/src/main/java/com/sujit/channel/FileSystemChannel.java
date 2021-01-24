package com.sujit.channel;

import com.sujit.Transaction;
import com.sujit.dataformat.Parser;
import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSystemChannel implements Channel {
  final Parser parser;
  final File fileName;

  public FileSystemChannel(Parser parser, File fileName) {
    this.parser = parser;
    this.fileName = fileName;
  }

  @Override
  public List<Transaction> read() {
    List<Transaction> transactions = null;
    try (InputStreamReader reader = new FileReader(fileName)) {
      transactions = parser.parse(reader);
    } catch (IOException ioException) {
      Logger.getGlobal()
          .log(
              Level.SEVERE,
              "An Exception Occurs on reading" + ioException.getMessage(),
              ioException);
    }
    return transactions;
  }

  @Override
  public void writeLine(String transaction) {
    try (FileWriter writer = new FileWriter(fileName, true)) {
      parser.transfer(transaction, writer);
    } catch (IOException ioException) {
      Logger.getGlobal()
          .log(
              Level.SEVERE,
              "An Exception Occurs on writing" + ioException.getMessage(),
              ioException);
    }
  }
}
