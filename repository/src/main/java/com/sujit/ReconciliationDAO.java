package com.sujit;

import com.sujit.dataformat.Transaction;
import java.util.List;

public interface ReconciliationDAO {
  List<Transaction> findAll();

  void saveRow(String transactions);
}
