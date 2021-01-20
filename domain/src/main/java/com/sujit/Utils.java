package com.sujit;

import com.sujit.dataformat.ParserType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Utils {
    private Utils(){}

    public static DateFormat getDateFormatterByParserType(ParserType parserType){
        DateFormat dateFormat = new SimpleDateFormat();
        if(parserType.equals(ParserType.CSV)) dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        else if(parserType.equals(ParserType.JSON)) dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat;
    }
}
