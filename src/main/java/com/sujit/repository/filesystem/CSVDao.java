package com.sujit.repository.filesystem;

import com.sujit.domain.Transaction;
import com.sujit.repository.ReconciliationDAO;

import java.util.List;

public class CSVDao implements ReconciliationDAO {
    public List<Transaction> findAll() {
        return null;
    }

    public Transaction find(String transactionId) {
        return null;
    }

    public boolean saveAll(List<Transaction> transactions) {
        return false;
    }

    public boolean save(Transaction transaction) {
        return false;
    }

    public boolean update(Transaction transaction) {
        return false;
    }
}
