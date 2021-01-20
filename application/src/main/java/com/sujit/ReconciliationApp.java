package com.sujit;


import java.util.Scanner;
import java.util.logging.Logger;

public class ReconciliationApp {

    public static void main(String[] args) {
        String fileFormat;

        //getting filenames as input
        String  sourceFile;
        String targetFile;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter Source file location with its format");
        sourceFile = scanner.nextLine().trim();
//        if(!sourceFile.matches("^*.[a-zA-Z0-9]+$")){
//            System.out.println("You haven't entered file format. Please Enter it");
//            sourceFile += scanner.next();
//        }
        System.out.println("Enter Target file location with its format");
        targetFile = scanner.nextLine().trim();

        //      /home/sujit/clusus/file1.csv
        //        /home/sujit/clusus/file2.json
        new Reconciliator().arrangeDataThenApplyReconciliation(sourceFile, targetFile);
        Logger.getGlobal().info("Reconciliation Finished");
        Logger.getGlobal().info("Result files are availble in directory /home/com.sujit/reconciliation-result");


    }

}