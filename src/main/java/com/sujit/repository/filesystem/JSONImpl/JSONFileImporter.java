package com.sujit.repository.filesystem.JSONImpl;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sujit.domain.Transaction;
import com.sujit.repository.filesystem.FileImporter;

import javax.sql.rowset.spi.TransactionalWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JSONFileImporter implements FileImporter {
    public List<Transaction> importFile(File fileName) {
        Transaction[] transactions = null;
        Gson gson = new Gson();
        try {
            transactions = gson.fromJson(new JsonReader(new FileReader(fileName)), Transaction[].class);


        }catch (IOException ioException){
            ioException.printStackTrace();
        }
        return Arrays.asList(transactions);

    }
}
