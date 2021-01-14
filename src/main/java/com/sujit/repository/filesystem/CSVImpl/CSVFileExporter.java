package com.sujit.repository.filesystem.CSVImpl;

import com.sujit.domain.Transaction;
import com.sujit.repository.filesystem.FileExporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVFileExporter implements FileExporter {
    public void export(List<Transaction> transactions, File fileName) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter( fileName, false))){
            CSVPrinter printer = CSVFormat.DEFAULT.withHeader("trans unique id","trans description","amount"
                    ,"currency","purpose","value date","trans type").print(writer);


            transactions.forEach(transaction -> {
                try {
                    printAllAtributeOnFile(transaction, printer);

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });

            printer.flush();
            writer.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public void export(Transaction transaction, File fileName) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter( fileName, false))) {
            CSVPrinter printer = CSVFormat.DEFAULT.withHeader("trans unique id", "trans description", "amount"
                    , "currency", "purpose", "value date", "trans type").print(writer);
            printAllAtributeOnFile(transaction, printer);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }


        }

    private void printAllAtributeOnFile(Transaction transaction, CSVPrinter printer) throws IOException {
        printer.print(transaction.getTransId());
        printer.print(transaction.getDescription());
        printer.print(transaction.getAmount());
        printer.print(transaction.getCurrencyCode());
        printer.print(transaction.getPurpose());
        printer.print(transaction.getDate());
        printer.print(transaction.getTransType());
        printer.println();
        printer.printRecords();
    }
}
