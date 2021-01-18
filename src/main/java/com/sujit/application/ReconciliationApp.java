package com.sujit.application;


import java.util.Scanner;

public class ReconciliationApp {

    public static void main(String[] args) {
        String fileFormat;

        //getting filenames as input
        String  sourceFile;
        String targetFile;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter Source file location with its format");
        sourceFile = scanner.nextLine().trim();
        System.out.println("Enter Target file location with its format");
        targetFile = scanner.nextLine().trim();

        //      /home/sujit/clusus/file1.csv
        //        /home/sujit/clusus/file2.json
        new Reconciliator().arrangeDataThenApplyReconciliation(sourceFile, targetFile);


    }

}
