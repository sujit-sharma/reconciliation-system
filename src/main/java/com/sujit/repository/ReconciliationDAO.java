package com.sujit.repository;

import com.sujit.domain.Transaction;

import java.util.List;

public interface ReconciliationDAO {
    public List<Transaction> findAll();
    public Transaction find(String transactionId);
    public void saveAll( List<Transaction> transactions);
    public void saveRow(String  transactions);
    public boolean save(Transaction transaction);
    public boolean update(Transaction transaction);
}
