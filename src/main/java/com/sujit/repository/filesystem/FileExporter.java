package com.sujit.repository.filesystem;

import com.sujit.domain.Transaction;

import java.io.File;
import java.util.List;

public interface FileExporter {
    public void export(List<Transaction> transactions, File fileName);
    public void export(Transaction transaction, File fileName);
}
