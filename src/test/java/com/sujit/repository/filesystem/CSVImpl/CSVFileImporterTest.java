//package com.sujit.repository.filesystem.CSVImpl;
//
//import com.sujit.domain.Transaction;
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVPrinter;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.text.ParseException;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class CSVFileImporterTest {
//    CSVFileImporter fileImporter;
//    List<Transaction> actualTransactionList;
//    private File actualTransData;
//
//    @BeforeEach
//    @Order(1)
//    public void init() throws IOException, ParseException {
//        fileImporter = new CSVFileImporter();
//        actualTransactionList = new ArrayList<>(5);
//        actualTransactionList.add(new Transaction("TR-47884222201", "online transfer",140D,"USD", "donation","2020-01-15" ,'D'));
//        actualTransactionList.add(new Transaction("TR-47884222202", "atm withdraw",20.0000,"JOD",null,"2020-01-20",'D'));
//        actualTransactionList.add(new Transaction("TR-47884222204", "salary",1200.000,"JOD","donation","2020-01-31",'C'));
//
//        actualTransData = new File("." + File.separator + "src" + File.separator + "test" + File.separator + "testFiles" + File.separator + "actualTransData.csv");
//
//        //Arranging file to import
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(actualTransData))) {
//
//            CSVPrinter printer = CSVFormat.DEFAULT.withHeader("trans unique id","trans description","amount"
//                    ,"currency","purpose","value date","trans type").print(writer);
//
//            actualTransactionList.stream().forEach(transaction -> {
//                try {
//                    printer.print(transaction.getTransId());
//                    printer.print(transaction.getDescription());
//                    printer.print(transaction.getAmount());
//                    printer.print(transaction.getCurrencyCode());
//                    printer.print(transaction.getPurpose());
//                    printer.print(transaction.getDate());
//                    printer.print(transaction.getTransType());
//                    printer.println();
//                    printer.printRecords();
//
//                } catch (IOException ioException) {
//                    ioException.printStackTrace();
//                }
//            });
//        }
//    }
//
//    @Test
//    public void givenFileNameWhenImportedShouldReturnListOfTransaction(){
//
//        assertNotSame(actualTransactionList, fileImporter.importFile(actualTransData));
//    }
//}