package com.sujit.repository.filesystem.JSONImpl;

import com.google.gson.Gson;
import com.sujit.domain.Transaction;
import com.sujit.repository.filesystem.FileExporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JSONFileExporter implements FileExporter {
    public void export(List<Transaction> transactions, File fileName) {

        Gson gson = new Gson();
        transactions.stream().forEach(transaction -> {
            try {
                gson.toJson(transaction, new FileWriter(fileName, true));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });


    }

    public void export(Transaction transaction, File fileName) {

    }
}
