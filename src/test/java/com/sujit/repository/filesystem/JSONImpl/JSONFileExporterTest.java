package com.sujit.repository.filesystem.JSONImpl;

import com.sujit.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JSONFileExporterTest {
    private List<Transaction> transactions;
    private JSONFileExporter fileExporter;
    private File testDirectory;

    @BeforeEach
    public void initFileSystem() {
        fileExporter = new JSONFileExporter();
        testDirectory = new File("." + File.separator + "src" + File.separator + "test" + File.separator + "testFiles");
        testDirectory.mkdir();

        transactions = new ArrayList<>(5);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        transactions.add(new Transaction("TR-47884222201", "online transfer",140D,"USD", "donation", LocalDate.parse("2020-01-15", dateFormat) ,'D'));
        transactions.add(new Transaction("TR-47884222202", "atm withdraw",20.0000,"JOD",null, LocalDate.parse("2020-01-20",dateFormat),'D'));
        transactions.add(new Transaction("TR-47884222204", "salary",1200.000,"JOD","donation", LocalDate.parse("2020-01-31", dateFormat),'C'));

    }

    @Test
    public void givenFileNameWhenExportedShouldCreateAFileWithGivenData() {

        fileExporter.export(transactions , new File(testDirectory, "exportedtest.json"));
    }

}