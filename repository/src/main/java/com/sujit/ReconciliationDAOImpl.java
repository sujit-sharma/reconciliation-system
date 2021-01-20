package com.sujit;



import com.sujit.channel.Channel;

import java.util.List;

public class ReconciliationDAOImpl implements ReconciliationDAO {

    private final Channel channel;

    public ReconciliationDAOImpl(Channel channel){
        this.channel = channel;
    }
    @Override
    public List<Transaction> findAll() {
        return channel.read();
    }

    @Override
    public void saveRow(String transaction) {
        channel.writeLine(transaction);

    }

}
