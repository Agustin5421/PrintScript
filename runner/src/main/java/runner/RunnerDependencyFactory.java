package runner;

import factory.LexerFactory;
import factory.ParserFactory;
import interpreter.ReworkedInterpreter;
import interpreter.ReworkedInterpreterFactory;
import lexer.Lexer;
import linter.Linter;
import linter.LinterFactory;
import observers.ProgressObserver;
import output.OutputResult;
import parsers.Parser;

import java.util.Formatter;

public class RunnerDependencyFactory {

    public static Parser getParserValidate(String version, String code, ProgressObserver progressObserver) {
        Lexer lexer = LexerFactory.getLexer(version);
        Parser parser = ParserFactory.getParser(version);
        parser = parser.setLexer(lexer.setInputAsString(code));

        return parser;
    }

    //TODO: interpreter should receive the parser
    public static ReworkedInterpreter getInterpreterExecute(String version, OutputResult<String> output, String code, ProgressObserver progressObserver) {
        return ReworkedInterpreterFactory.buildInterpreter(version, output);
    }

    public static Formatter getFormatterFormat(String version, String code, ProgressObserver progressObserver) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static Linter getLinterAnalyze(String version, String code, String config, ProgressObserver progressObserver) {
        return LinterFactory.getLinter(version, config).setInput(code);
    }

}
