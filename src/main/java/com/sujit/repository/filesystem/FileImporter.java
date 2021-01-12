package com.sujit.repository.filesystem;

import com.sujit.domain.Transaction;

import java.io.File;
import java.util.List;

public interface FileImporter {
    public List<Transaction> importFile(File fileName);
}
