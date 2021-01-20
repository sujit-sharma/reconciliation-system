package com.sujit.channel;

import com.sujit.Transaction;

import java.util.List;

public interface Channel {
    public List<Transaction> read();
    public void write(List<Transaction> transactions);
    public void writeLine(String transaction);
}
