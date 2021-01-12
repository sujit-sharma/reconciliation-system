package com.sujit.application.parser;

import com.sujit.domain.Transaction;

import java.util.List;

public interface Parser {
    public List<Transaction> parse();
    public void unparse(List<Transaction> transactions);
}
