package com.sujit.dataformat;

import com.sujit.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoogleJsonParserTest {

private GoogleJsonParser parser;
private String inputFileName;

    @BeforeEach
    void setUP() {
        parser = new GoogleJsonParser();
        inputFileName = "input.json";
    }

    private InputStream readAsStream(String name) {
        return getClass().getClassLoader().getResourceAsStream(name);
    }

    @Test
    void givenJsonFileWhenParseThenShouldReturnTxnList() {
        InputStream reader = readAsStream(inputFileName);

        List<Transaction> result  = parser.parse(new InputStreamReader(reader));

        assertEquals(7, result.size());

    }

    @Test
    void givenJsonFileWhenParseThenReturnTxnListShouldHaveSameValuesAsFile() {
        InputStream reader = readAsStream(inputFileName);

        List<Transaction> result  = parser.parse(new InputStreamReader(reader));
        Transaction firstTxn = result.get(0);

        assertEquals("TR-47884222201", firstTxn.getTransId());
        assertEquals(Double.valueOf("140.00"), firstTxn.getAmount());
        assertEquals(Currency.getInstance("USD"), firstTxn.getCurrencyCode());
        assertEquals("donation", firstTxn.getPurpose());
        assertEquals(LocalDate.parse("2020-01-20"), firstTxn.getDate());
        assertEquals(null, firstTxn.getDescription());

        Transaction lastTxn = result.get(result.size() - 1);

        assertEquals("TR-47884222245", lastTxn.getTransId());
        assertEquals(null, lastTxn.getDescription());
        assertEquals(Double.valueOf("420"), lastTxn.getAmount());
        assertEquals(Currency.getInstance("USD"), lastTxn.getCurrencyCode());
        assertEquals("loan", lastTxn.getPurpose());
        assertEquals(LocalDate.parse("2020-01-12"), lastTxn.getDate());

    }


}