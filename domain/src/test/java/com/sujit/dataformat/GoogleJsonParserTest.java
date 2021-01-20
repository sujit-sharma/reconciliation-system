package com.sujit.dataformat;

import org.junit.jupiter.api.Test;

import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

class GoogleJsonParserTest {
    Parser parser;
    File testFile;

    @Test
    public void init() {
        parser = new GoogleJsonParser();
        testFile = new File("." + File.separator + "src" + File.separator + "test" + File.separator + "testFiles" + File.separator + "input.csv");
    }

    @Test
    public void givenFileReader_WhenParse_ThenShouldReturnListOfTransaction() throws IOException {
        parser.parse(new FileReader(testFile));
        Logger.getGlobal().info("Test Run");
    }

}