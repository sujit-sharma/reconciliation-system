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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

public class Reconciliator {
    private static final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

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
        String destinationDir = "/home/sujit/clusus/reconcialited-result";
        for ( File file: new File(destinationDir).listFiles() ) {
            file.delete();
        }
       new File(destinationDir).delete();
        new File(destinationDir).mkdir();
       // arranging system to write in different files
        ReconciliationDAO matchingDao = new ReconciliationDAOImpl(new FileSystemChannel(new ApacheCsvParser(), new File(destinationDir + "/MatchingTransactions.csv")));
        matchingDao.saveRow("transaction id,amount,currency code,value date");
        ReconciliationDAO mismatchingDao = new ReconciliationDAOImpl(new FileSystemChannel(new ApacheCsvParser(), new File(destinationDir + "/MismatchingTransactions.csv")));
        mismatchingDao.saveRow("found in file,transaction id,amount,currency code,value date");
        ReconciliationDAO missingDao = new ReconciliationDAOImpl(new FileSystemChannel(new ApacheCsvParser(), new File(destinationDir + "/MissingTransactions.csv")));
        missingDao.saveRow("found in file,transaction id,amount,currency code,value date");

        for (Iterator<Transaction> sourceItr = sourceList.listIterator(); sourceItr.hasNext(); ) {
            Transaction sourceTrans = sourceItr.next();

            for (Iterator<Transaction> targetItr = targetList.listIterator(); targetItr.hasNext(); ) {
                Transaction targetTrans = targetItr.next();
                if(sourceTrans.getTransId().equals(targetTrans.getTransId())){
                    if(sourceTrans.getAmount().equals(targetTrans.getAmount())
                            && sourceTrans.getCurrencyCode().equals(targetTrans.getCurrencyCode())
                            && sourceTrans.getDate().equals(targetTrans.getDate())
                            ) {
                        String row = sourceTrans.getTransId() + COMMA + sourceTrans.getAmount() + COMMA
                                + sourceTrans.getCurrencyCode() + COMMA + dateFormatter.format(sourceTrans.getDate());
                        matchingDao.saveRow(row);
                        sourceItr.remove();
                        targetItr.remove();
                    }
                    else {
                        String row1 = "SOURCE" + COMMA + sourceTrans.getTransId() + COMMA + sourceTrans.getAmount() + COMMA
                                + sourceTrans.getCurrencyCode() + COMMA + dateFormatter.format(sourceTrans.getDate());
                        mismatchingDao.saveRow(row1);
                        sourceItr.remove();
                        String row2 = "TARGET" + COMMA + targetTrans.getTransId() + COMMA + targetTrans.getAmount() + COMMA
                                + targetTrans.getCurrencyCode() + COMMA + dateFormatter.format(targetTrans.getDate());
                        mismatchingDao.saveRow(row2);
                        targetItr.remove();
                    }

                }

            }

        }
        sourceList.forEach(transaction -> {
            String row1 = "SOURCE" + COMMA + transaction.getTransId() + COMMA + transaction.getAmount() + COMMA
                    + transaction.getCurrencyCode() + COMMA + dateFormatter.format(transaction.getDate());
            missingDao.saveRow(row1);
        });
        targetList.forEach(transaction -> {
            String row1 = "TARGET" + COMMA + transaction.getTransId() + COMMA + transaction.getAmount() + COMMA
                    + transaction.getCurrencyCode() + COMMA + dateFormatter.format(transaction.getDate());
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
