package com.sujit.domain.channel;

import com.sujit.domain.Transaction;

import java.io.File;
import java.util.List;

public interface Channel {
    public List<Transaction> read();
    public void write(List<Transaction> transactions);
    public void writeLine(String transaction);
}
