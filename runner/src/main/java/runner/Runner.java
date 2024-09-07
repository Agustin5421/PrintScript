package runner;

import factory.LexerFactory;
import factory.ParserFactory;
import lexer.Lexer;
import parsers.Parser;

public class Runner {
    public void execute (String codeFilepath, String version){
        //TODO: need factory for executor

    }

    public void analyze (String codeFilepath, String version, String optionsFilepath, OutputResult output){
        //TODO: need factory for analyzer

    }

    public void format (String codeFilepath, String version, String optionsFilepath){
        //TODO: need factory for formatter

    }

    public void validate (String input, String version){
        //TODO: lexer should receive codeFilePath
        Lexer lexer = LexerFactory.getLexer(version);
        Parser parser = ParserFactory.getParser(version);

        parser = parser.setLexer(lexer.setInput(input));
    }
}
