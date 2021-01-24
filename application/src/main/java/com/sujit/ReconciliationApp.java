package com.sujit;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class ReconciliationApp {

  public static void main(String[] args) throws IOException {
    // getting filenames as input
    String sourceFile;
    String targetFile;

    Scanner scanner = new Scanner(System.in);
    Logger.getGlobal().info("Please Enter Source file location with its format");
    sourceFile = scanner.nextLine().trim();
    Logger.getGlobal().info("Enter Target file location with its format");
    targetFile = scanner.nextLine().trim();

    new ReconciliationProcessor().arrangeDataThenApplyReconciliation(sourceFile, targetFile);
    Logger.getGlobal().info("Reconciliation Finished");
    Logger.getGlobal()
        .info("Result files are available in directory /home/com.sujit/reconciliation-result");
  }
}
