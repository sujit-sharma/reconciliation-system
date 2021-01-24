package com.sujit.parser;

import com.sujit.dataformat.Transaction;
import java.io.*;
import java.util.List;

public interface Parser {
  List<Transaction> parse(InputStreamReader reader) throws IOException;

  void transfer(String line, FileWriter writer) throws IOException;
}
