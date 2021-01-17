package com.sujit.repository;

import com.sujit.domain.Transaction;
import com.sujit.domain.channel.Channel;

import java.util.List;

public class ReconciliationDAOImpl implements ReconciliationDAO {

    private Channel channel;

    public ReconciliationDAOImpl(Channel channel){
        this.channel = channel;
    }
    @Override
    public List<Transaction> findAll() {
        return channel.read();
    }

    @Override
    public void saveAll(List<Transaction> transactions) {
        channel.write(transactions);
    }

    @Override
    public Transaction find(String transactionId) {
        return null;
    }


    @Override
    public boolean save(Transaction transaction) {
        return false;
    }

    @Override
    public boolean update(Transaction transaction) {
        return false;
    }
}
