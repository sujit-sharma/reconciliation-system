package com.sujit.dataformat;

import com.sujit.Transaction;

import java.io.*;
import java.util.List;

public interface Parser {
    List<Transaction> parse(FileReader reader) throws IOException;
    void transfer(List<Transaction> transactions, BufferedWriter writer) throws IOException;
    void transfer(String line, FileWriter writer) throws IOException;
}
