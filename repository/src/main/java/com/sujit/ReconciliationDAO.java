package com.sujit;
import java.util.List;

public interface ReconciliationDAO {
    List<Transaction> findAll();

    void saveRow(String transactions);
}
