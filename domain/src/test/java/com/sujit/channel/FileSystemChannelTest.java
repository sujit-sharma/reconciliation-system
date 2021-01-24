package com.sujit.channel;

import com.sujit.Transaction;
import com.sujit.dataformat.ApacheCsvParser;
import com.sujit.dataformat.GoogleJsonParser;
import com.sujit.dataformat.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemChannelTest {

    private FileSystemChannel channel;
    private Parser parser;
    private File file;

    @BeforeEach
    void destroy(){
        if(file != null) file.delete();
    }

    @Test
    void whenCalledReadWithCsvFileWithCsvParserShouldReturnListOfTransactions() {
        parser = new ApacheCsvParser();
        file = new File( "."+File.separator + "src" + File.separator+"test"+File.separator+"resources"+File.separator+"input.csv");
        channel = new FileSystemChannel(parser, file);
        List<Transaction> result = channel.read();

        assertEquals(6, result.size());
    }
    @Test
    void whenCalledReadWithCsvFileWithJsonParserShouldThrowException() {
        parser = new GoogleJsonParser();
        file = new File( "."+File.separator + "src" + File.separator+"test"+File.separator+"resources"+File.separator+"input.csv");
        channel = new FileSystemChannel(parser, file);
        Exception exception = assertThrows(Exception.class, () -> {
            List<Transaction> result = channel.read();
        });

    }

    @Test
    void whenCalledReadWithJsonFileWithJsonParserShouldReturnListOfTransactions() {
        parser = new GoogleJsonParser();
        file = new File( "."+File.separator + "src" + File.separator+"test"+File.separator+"resources"+File.separator+"input.json");
        channel = new FileSystemChannel(parser, file);
        List<Transaction> result = channel.read();

        assertEquals(7, result.size());
    }

    @Test
    void whenCalledReadWithJsonFileWithCsvParserShouldThrowException() {
        parser = new ApacheCsvParser();
        file = new File( "."+File.separator + "src" + File.separator+"test"+File.separator+"resources"+File.separator+"input.json");
        channel = new FileSystemChannel(parser, file);
        Exception exception = assertThrows(Exception.class, () -> {
            List<Transaction> result = channel.read();
        });

    }

    @Test
    void givenStringTxnWhenWriteLineShouldWriteStringToFile() throws IOException {
        parser = new ApacheCsvParser();
        file = new File( "."+File.separator + "src" + File.separator+"test"+File.separator+"resources"+File.separator+"fileChannelOutput.csv");
        channel = new FileSystemChannel(parser, file);
        final String COMMA = ",";
        Transaction expected =
                new Transaction(
                        "TR-47884222201",
                        "online transfer",
                        Double.parseDouble("140"),
                        Currency.getInstance("USD"),
                        "donation",
                        LocalDate.parse("2020-01-20"),
                        'D');
        String row =
                expected.getTransId()
                        + COMMA
                        + expected.getAmount()
                        + COMMA
                        + expected.getCurrencyCode()
                        + COMMA
                        + expected.getDate();
        channel.writeLine(row);
        byte[] actual = Files.readAllBytes(file.toPath());

        assertEquals(row + "\n", new String(actual));

        file.delete();

    }

}