package com.sujit;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * IT IS WORKING ON MY MACHINE - GET RID OF THIS FILE NIO 2 FAT JAR - Bundle jar with all
 * dependencies plus mainFest with main class (single jar as an application) Jacoco - Test Coverage
 */
public class ReconciliationApp {

  public static void main(String[] args) throws IOException {
    String sourceFile;
    String targetFile;
    Scanner scanner = new Scanner(System.in);
    Logger.getGlobal().info("Please Enter Source file location with its format");
    sourceFile = scanner.nextLine().trim();
    Logger.getGlobal().info("Enter Target file location with its format");
    targetFile = scanner.nextLine().trim();

    String homeDir = ".";

    new ReconciliationProcessor(homeDir).arrangeDataThenApplyReconciliation(sourceFile, targetFile);
    Logger.getGlobal().info("Reconciliation Finished");
    Logger.getGlobal()
        .info(
            () ->
                String.format(
                    "Result files are available in directory %s/reconciliation-result", homeDir));
  }
}
