package com.sujit.domain.dataformat;

import com.sujit.domain.Transaction;

import java.io.*;
import java.util.List;

public interface Parser {
    public List<Transaction> parse(FileReader reader) throws IOException;
    public void transfer(List<Transaction> transactions, BufferedWriter writer) throws IOException;
}
