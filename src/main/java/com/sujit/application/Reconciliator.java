package com.sujit.application;

import com.sujit.domain.Transaction;
import com.sujit.domain.channel.Channel;
import com.sujit.domain.channel.FileSystemChannel;
import com.sujit.domain.dataformat.ApacheCsvParser;
import com.sujit.domain.dataformat.GoogleJsonParser;
import com.sujit.domain.dataformat.Parser;
import com.sujit.repository.ReconciliationDAO;
import com.sujit.repository.ReconciliationDAOImpl;

import java.io.File;
import java.util.List;

public class Reconciliator {

    public void arrangeDataThenApplyReconciliation(String source, String target) {
        List<Transaction> sourceList =  accessData(source);
        //for target file
        List<Transaction> targetList = accessData(target);
        //System.out.println(sourceList);
        //System.out.println(targetList);

        Reconciliate(sourceList, targetList);
    }

    private synchronized void Reconciliate(List<Transaction> sourceList, List<Transaction> targetList) {
        final String COMMA = ",";
        String destinationDir = "/home/sujit/clusus";
       // arranging system to write in different files
        ReconciliationDAO matchingDao = new ReconciliationDAOImpl(new FileSystemChannel(new ApacheCsvParser(), new File(destinationDir + "/MatchingTransactions.csv")));
        matchingDao.saveRow("transaction id,amount,currency code,value date");
        ReconciliationDAO mismatchingDao = new ReconciliationDAOImpl(new FileSystemChannel(new ApacheCsvParser(), new File(destinationDir + "/MismatchingTransactions.csv")));
        mismatchingDao.saveRow("found in file,transaction id,amount,currency code,value date");
        ReconciliationDAO missingDao = new ReconciliationDAOImpl(new FileSystemChannel(new ApacheCsvParser(), new File(destinationDir + "/MissingTransactions.csv")));
        missingDao.saveRow("found in file,transaction id,amount,currency code,value date");

        for (Transaction sourceTrans: sourceList ) {

            for (Transaction targetTrans : targetList ) {
                if(sourceTrans.getTransId().equals(targetTrans.getTransId())){
                    if(sourceTrans.getAmount().equals(targetTrans.getAmount())
                            && sourceTrans.getCurrencyCode().equals(targetTrans.getCurrencyCode())
                            && sourceTrans.getDate().equals(targetTrans.getDate())
                            ) {
                        String row = sourceTrans.getTransId() + COMMA + sourceTrans.getAmount() + COMMA
                                + sourceTrans.getCurrencyCode() + COMMA + sourceTrans.getDate();
                        matchingDao.saveRow(row);
                        //targetList.remove(targetTrans); //remove processed element to reduce complexity
                        //sourceList.remove(sourceTrans);

                    }
                    else {
                        String row1 = "SOURCE" + COMMA + sourceTrans.getTransId() + COMMA + sourceTrans.getAmount() + COMMA
                                + sourceTrans.getCurrencyCode() + COMMA + sourceTrans.getDate();
                        mismatchingDao.saveRow(row1);
                        //sourceList.remove(sourceTrans);
                        String row2 = "TARGET" + COMMA + sourceTrans.getTransId() + COMMA + sourceTrans.getAmount() + COMMA
                                + sourceTrans.getCurrencyCode() + COMMA + sourceTrans.getDate();
                        mismatchingDao.saveRow(row2);
                        //targetList.remove(targetTrans);

                    }

                }

            }

        }
        sourceList.forEach(transaction -> {
            String row1 = "SOURCE" + COMMA + transaction.getTransId() + COMMA + transaction.getAmount() + COMMA
                    + transaction.getCurrencyCode() + COMMA + transaction.getDate();
            missingDao.saveRow(row1);
        });
        targetList.forEach(transaction -> {
            String row1 = "TARGET" + COMMA + transaction.getTransId() + COMMA + transaction.getAmount() + COMMA
                    + transaction.getCurrencyCode() + COMMA + transaction.getDate();
            missingDao.saveRow(row1);
        });

    }

    private List<Transaction> accessData(String filePath) {
        String fileFormat  = filePath.substring(filePath.indexOf('.') + 1 );
        Parser parser;
        parser  = fileFormat.equalsIgnoreCase("csv") ? new ApacheCsvParser(): new GoogleJsonParser();
        Channel sourceChannel = new FileSystemChannel(parser, new File(filePath));
        ReconciliationDAO sourceDao = new ReconciliationDAOImpl(sourceChannel);
        return  sourceDao.findAll();
    }
}
