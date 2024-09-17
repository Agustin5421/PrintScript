package runner;

import factory.LexerFactory;
import factory.ParserFactory;
import interpreter.Interpreter;
import interpreter.IterableInterpreter;
import interpreter.factory.InterpreterFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import lexer.Lexer;
import linter.Linter;
import linter.LinterFactory;
import output.OutputResult;
import parsers.Parser;

public class Runner {

  public void execute(InputStream code, String version,OutputResult printLog, OutputResult errorLog) {
    IterableInterpreter iterableInterpreter = new IterableInterpreter(version, code);

    try {
      List<String> nextPrints;
      while(iterableInterpreter.hasNext()) {
        nextPrints = iterableInterpreter.next();
        for (String print : nextPrints) {
          printLog.saveResult(print);
        }
      }
    } catch (Throwable e) {
      iterableInterpreter = null;
      System.gc();
      System.out.println(e.getMessage());
      errorLog.saveResult(e.getMessage());
    }
  }

  public void analyze(InputStream code, String version, String config, OutputResult output) {
    Linter linter = LinterFactory.getLinter(version, config);
    linter = linter.setInputStream(code);

    while (linter.hasNext()) {
      output.saveResult(linter.next().toString());
    }
  }

  public void format(InputStream code, String version, String config) {
    // TODO: need factory for formatter

  }

  public void validate(InputStream input, String version) {
    // TODO: lexer should receive codeFilePath
    Lexer lexer = LexerFactory.getLexer(version);
    Parser parser = ParserFactory.getParser(version);

    assert lexer != null;
      try {
          parser = parser.setLexer(lexer.setInput(input));
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
  }
}
