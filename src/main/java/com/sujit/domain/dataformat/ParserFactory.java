package com.sujit.domain.dataformat;

public class ParserFactory {
    private Parser parser;

    public Parser getParser(String name) {
        if(name.equalsIgnoreCase("CSV")) parser =  new ApacheCsvParser();
        else if(name.equalsIgnoreCase("JSON")) parser = new GoogleJsonParser();
        return parser;
    }


}
