package com.sujit.dataformat;

public class ParserFactory {
    private ParserFactory() {}

    public static Parser getParserByName(ParserType name) {
        Parser parser = null;
        if(name.equals(ParserType.CSV)) parser = new ApacheCsvParser();
        else if(name.equals(ParserType.JSON)) parser = new GoogleJsonParser();
        return parser;
    }

}
