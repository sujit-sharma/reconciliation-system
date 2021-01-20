package com.sujit;

import com.sujit.dataformat.ParserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Logger;


import static org.junit.jupiter.api.Assertions.*;

class ReconciliatorTest {
    private Reconciliator reconciliator;
    private List<Transaction> sourceList;
    private List<Transaction> targetList;
    File testDirectory;
    private final DateFormat csvDateFormat = Utils.getDateFormatterByParserType(ParserType.CSV);
    private final DateFormat jsonDateFormat = Utils.getDateFormatterByParserType(ParserType.JSON);

    @BeforeEach
    public void init() throws ParseException {
        reconciliator = new Reconciliator();
        testDirectory = new File("." + File.separator + "src" + File.separator + "test" + File.separator + "testFiles");

        sourceList = new LinkedList<>();
        sourceList.add(new Transaction("TR-47884222201", "online transfer",140D, Currency.getInstance("USD"), "donation",csvDateFormat.parse("2020-01-15") ,'D'));
        sourceList.add(new Transaction("TR-47884222202", "atm withdraw",20.0000,Currency.getInstance("JOD"),"travel",csvDateFormat.parse("2020-01-20"),'D'));
        sourceList.add(new Transaction("TR-47884222204", "salary",1200.000,Currency.getInstance("JOD"),"donation",csvDateFormat.parse("2020-01-31"),'C'));

        targetList = new LinkedList<>();
        targetList.add(new Transaction("TR-47884222201", "online transfer",140D,Currency.getInstance("USD"), "donation",jsonDateFormat.parse("15/01/2020") ,'D'));
        targetList.add(new Transaction("TR-47884222202", "atm withdraw",30.0000,Currency.getInstance("JOD"),"travel",csvDateFormat.parse("2020-01-20"),'D'));
        targetList.add(new Transaction("TR-47884222205", "salary",1200.000,Currency.getInstance("JOD"),"donation",csvDateFormat.parse("2020-01-31"),'C'));

        Map<String, List<Transaction>> resultMap = compareData();
        Logger.getGlobal().info(resultMap.toString());

    }

    private Map<String ,List<Transaction>> compareData(){
        Map<String, List<Transaction>> transactionMap = new LinkedHashMap<>();
        List<Transaction> matching = new LinkedList<>();
        matching.add(sourceList.get(0));
        List<Transaction> mismatching = new LinkedList<>();
        mismatching.add(sourceList.get(1));
        mismatching.add(targetList.get(1));
        List<Transaction> missing = new LinkedList<>();
        missing.add(sourceList.get(2));
        missing.add(targetList.get(2));

        transactionMap.put("matching", matching);
        transactionMap.put("mismatching", mismatching);
        transactionMap.put("missing", missing);

        return transactionMap;
    }
    @Test
    public void given2FilesWhenExecutedThenShouldProvideTwoListOfTransactions() throws IOException {
        Reconciliator reconciliator = new Reconciliator();
        reconciliator.arrangeDataThenApplyReconciliation("/home/sujit/clusus/file1.csv", "/home/sujit/clusus/file2.json");
    }
    @Test
    public void givenListOfSourceAndTargetWithDestinationDir_WhenReconciliate_ShouldGenerate3Files() {
        reconciliator.reconciliate(sourceList, targetList);

        assertEquals(Objects.requireNonNull(testDirectory.list()).length, 3);
    }
    @Test
    public void givenListOfSourceAndTargetWithDestinationDir_WhenReconciliate_ShouldSeparateTransactionOnMatchingMismatchingAndMissingFiles() {
        reconciliator.reconciliate(sourceList,targetList);

    }

}