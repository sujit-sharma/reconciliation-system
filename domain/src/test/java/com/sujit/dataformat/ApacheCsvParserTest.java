package com.sujit.dataformat;

import com.sujit.Transaction;
import com.sujit.Utils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;

import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class ApacheCsvParserTest {
    Parser parser;
    List<Transaction> transactions;
    final DateFormat dateFormat = Utils.getDateFormatterByParserType(ParserType.CSV);
    File testFile;
    CSVPrinter printer;

    @BeforeEach
    public void init() throws ParseException, IOException{
        parser = new ApacheCsvParser();
        transactions = new LinkedList<>();
        transactions.add(new Transaction("TR-47884222201", "online transfer",140D, Currency.getInstance("USD"), "donation",dateFormat.parse("2020-01-15") ,'D'));
        transactions.add(new Transaction("TR-47884222202", "atm withdraw",20.0000,Currency.getInstance("JOD"),"travel",dateFormat.parse("2020-01-20"),'D'));
        transactions.add(new Transaction("TR-47884222204", "salary",1200.000,Currency.getInstance("JOD"),"donation",dateFormat.parse("2020-01-31"),'C'));

        testFile = new File("." + File.separator + "src" + File.separator + "test" + File.separator + "testFiles" + File.separator + "input.csv");
        //Arranging Files System

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {

            printer = CSVFormat.DEFAULT.withHeader("trans unique id","trans description","amount"
                    ,"currency","purpose","value date","trans type").print(writer);

            transactions.forEach(this::writeIntoFile);
        }
    }


    @AfterEach
    public void destroy() {
        File directory = new File(testFile.getParent());
        String[] Files =  directory.list();
        for(String currentFilePath : Objects.requireNonNull(Files)){
            File currentFile = new File(directory.getPath(), currentFilePath);
            assertTrue(currentFile.delete());
        }
    }
    public void writeIntoFile(Transaction transaction) {
        try {
            printer.print(transaction.getTransId());
            printer.print(transaction.getDescription());
            printer.print(transaction.getAmount());
            printer.print(transaction.getCurrencyCode());
            printer.print(transaction.getPurpose());
            printer.print(dateFormat.format(transaction.getDate()));
            printer.print(transaction.getTransType());
            printer.println();
            printer.printRecords();

        } catch (IOException ioException) {
            Logger.getGlobal().severe("An Exception Occurs" + ioException.getMessage());
        }
    }


    @Test
    public void givenCsvFileWhenParseThenShouldReturnListOfTransaction() throws IOException {
        FileReader reader = new FileReader(testFile);
        reader.close();

    }

    @Test
    public void givenCsvFileWithNullOnWhenParsedShouldReturnListOfTransactions() throws IOException ,ParseException {
        FileReader reader = new FileReader(testFile);
        Transaction transaction = new Transaction("TR-47884222205","atm withdrwal",60.0,Currency.getInstance("JOD"),null,dateFormat.parse("2020-02-02"),'D');
        transactions.add(transaction);
        writeIntoFile(transaction);

        assertEquals(transactions, parser.parse(reader));
    }

    @Test
    public void givenListOfTransactionsAndFileWriterShouldWriteAllTransactionOnFile() throws IOException {
        parser.transfer(transactions, new BufferedWriter(new FileWriter(testFile.getParent() + "/actual.csv")));
        File actualFile  = new File(testFile.getParent() + "/actual.csv");
        byte[] actual = Files.readAllBytes(actualFile.toPath());
        byte[] expected = Files.readAllBytes(testFile.toPath());

        assertEquals(new String(actual), new String(expected));
    }

    @Test
    public void givenRowOfCsvFileAndFileWriter_WhenTransfer_ThenShouldWriteRowInWriter() throws IOException {
        String line  = transactions.get(0).getTransId() + "," +transactions.get(0).getDescription() + ","
                + transactions.get(0).getAmount() + "," + transactions.get(0).getCurrencyCode() + ","
                +transactions.get(0).getPurpose() + "," + dateFormat.format(transactions.get(0).getDate()) + "," + transactions.get(0).getTransType();

        parser.transfer(line,new FileWriter(testFile.getParent() + "/aline.csv"));
        FileReader actualFile  = new FileReader(testFile.getParent() + "/aline.csv");
        FileReader expectedFile  = new FileReader(testFile);
        BufferedReader br = new BufferedReader(actualFile);
        String actualLine  = br.readLine();
        br = new BufferedReader(expectedFile);
        br.readLine(); //neglecting header
        String expectedLine = br.readLine();

        assertEquals(expectedLine , actualLine );
    }
}