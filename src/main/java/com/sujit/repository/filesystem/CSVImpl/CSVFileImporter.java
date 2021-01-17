package com.sujit.repository.filesystem.CSVImpl;

import com.sujit.domain.Transaction;
import com.sujit.repository.filesystem.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CSVFileImporter implements FileImporter {

    public List<Transaction> importFile(File fileName) {

        List<Transaction> transactionList = new ArrayList<>(100);
        try(BufferedReader br  = new BufferedReader(new FileReader(fileName))){
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(br);
            for (CSVRecord attribute : records ) {
                Transaction transaction = new Transaction();
                transaction.setTransId(attribute.get("trans unique id"));
                transaction.setDescription(attribute.get("trans description"));
                transaction.setAmount(Double.parseDouble(attribute.get("amount")));
                transaction.setCurrencyCode(attribute.get("currency"));
                transaction.setPurpose(attribute.get("purpose"));
                transaction.setDate(attribute.get("value date"));
                transaction.setTransType((attribute.get("trans type").charAt(0)));

                transactionList.add(transaction);
            }

        }catch (IOException ioException){
            ioException.printStackTrace();
        }

        return transactionList;
    }
}
