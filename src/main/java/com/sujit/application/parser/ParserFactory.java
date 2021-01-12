package com.sujit.application.parser;

public class ParserFactory {
    private Parser parser;

    public Parser getParser(String name) {
        if(name.equalsIgnoreCase("CSV")) parser =  new CsvParser();
        else if(name.equalsIgnoreCase("JSON")) parser = new JsonParser();
        return parser;
    }


}
