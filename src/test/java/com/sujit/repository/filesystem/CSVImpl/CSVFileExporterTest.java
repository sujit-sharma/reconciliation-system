package com.sujit.repository.filesystem.CSVImpl;

import com.sujit.domain.Transaction;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVFileExporterTest {
   // private CSVPrinter csvPrinter;
    private CSVFileExporter csvFileExporter;
    private File testDirectory;
    private List<Transaction> transactions;

    @BeforeEach
    public void init() throws ParseException {
        csvFileExporter = new CSVFileExporter();
        transactions = new ArrayList<>(5);
        transactions.add(new Transaction("TR-47884222201", "online transfer",140D,"USD", "donation","2020-01-15",'D'));
        transactions.add(new Transaction("TR-47884222202", "atm withdraw",20.0000,"JOD",null,"2020-01-20",'D'));
        transactions.add(new Transaction("TR-47884222204", "salary",1200.000,"JOD","donation", "2020-01-31",'C'));

        testDirectory = new File("." + File.separator + "src" + File.separator + "test" + File.separator + "testFiles");
        testDirectory.mkdir();

    }
//    @AfterEach
//    public void destroy() {
//        String[] Files =  testDirectory.list();
//        for(String currentFilePath : Files ){
//            File currentFile  = new File(testDirectory.getPath(), currentFilePath);
//            currentFile.delete();
//        }
//        testDirectory.delete();
//    }

    @Test
    public void givenListOfTransactionsAndFileNameWhenExportedShouldHaveFileContainsAllDataOfListInCSVFormat() {

    csvFileExporter.export(transactions, new File(testDirectory, "expoettest.csv"));

    }
}