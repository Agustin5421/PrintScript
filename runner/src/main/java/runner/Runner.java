package runner;

import factory.LexerFactory;
import factory.ParserFactory;
import interpreter.ReworkedInterpreter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import interpreter.factory.ReworkedInterpreterFactory;
import lexer.Lexer;
import linter.Linter;
import linter.LinterFactory;
import output.OutputResult;
import parsers.Parser;

public class Runner {
  public void execute(InputStream code, String version,OutputResult<String> printLog, OutputResult<String> errorLog) {
    Lexer lexer;
    try {
      lexer = Objects.requireNonNull(LexerFactory.getLexer(version)).setInput(code);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    Parser parser = ParserFactory.getParser(version).setLexer(lexer);
    ReworkedInterpreter interpreter = ReworkedInterpreterFactory.buildInterpreter(version, printLog);

    try {
      while (parser.hasNext()) {
        interpreter = interpreter.interpret(parser.next());
      }
    } catch (Throwable e) {
      lexer = null;
      parser = null;
      interpreter = null;
      System.gc();
      errorLog.saveResult(e.getMessage());
    }
  }

  public void analyze(InputStream code, String version, String config, OutputResult<String> output) {
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
