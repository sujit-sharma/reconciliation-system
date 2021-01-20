package com.sujit;
import java.util.List;

public interface ReconciliationDAO {
    List<Transaction> findAll();
    void saveAll(List<Transaction> transactions);
    void saveRow(String transactions);
}
