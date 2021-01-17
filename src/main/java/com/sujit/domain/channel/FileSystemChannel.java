package com.sujit.domain.channel;

import com.sujit.domain.Transaction;
import com.sujit.domain.dataformat.Parser;

import java.io.*;
import java.util.List;

public class FileSystemChannel implements Channel {
    Parser parser;
    File fileName;

    public FileSystemChannel(Parser parser, File fileName){
        this.parser = parser;
        this.fileName = fileName;
    }

    @Override
    public List<Transaction> read() {
        try(FileReader reader = new FileReader(fileName)){
            parser.parse(reader);

        }catch (IOException ioException){
            ioException.printStackTrace();
        }

        return null;
    }

    @Override
    public void write(List<Transaction> transactions) {
        try( BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){

            parser.transfer(transactions, writer);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }

    }
}
