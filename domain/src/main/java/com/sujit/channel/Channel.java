package com.sujit.channel;

import com.sujit.dataformat.Transaction;
import java.util.List;

public interface Channel {
  List<Transaction> read();

  void writeLine(String transaction);
}
