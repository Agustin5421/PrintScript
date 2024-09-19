package runner;

import factory.LexerFactory;
import factory.ParserFactory;
import input.InputHandler;
import interpreter.ReworkedInterpreter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import interpreter.ReworkedInterpreterFactory;
import interpreter.visitor.staticprovider.Inputs;
import lexer.Lexer;
import linter.Linter;
import linter.LinterFactory;
import observers.ProgressObserver;
import observers.ProgressPrinter;
import output.OutputResult;
import parsers.Parser;

public class Runner {
  private final ProgressObserver progressObserver;

  public Runner(ProgressObserver progressObserver) {
    this.progressObserver = progressObserver;
  }

  public Runner() {
    this.progressObserver = new ProgressObserver(new ProgressPrinter());
  }

  public void execute(InputStream code, String version, OutputResult<String> printLog,
                      OutputResult<String> errorLog, InputHandler inputs) {
    Lexer lexer;
    try {
      lexer = Objects.requireNonNull(LexerFactory.getLexer(version)).setInput(code);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    Parser parser = ParserFactory.getParser(version).setLexer(lexer);
    ReworkedInterpreter interpreter = ReworkedInterpreterFactory.buildInterpreter(version, printLog);

    Inputs.setInputs(inputs);

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

  public void validate(InputStream input, String version) throws IOException {
    // TODO: lexer should receive codeFilePath
    Lexer lexer = LexerFactory.getLexer(version);
    Parser parser = ParserFactory.getParser(version);

    assert lexer != null;
    lexer = lexer.setInputWithObserver(input, progressObserver);

    parser = parser.setLexer(lexer);

    while (parser.hasNext()) {
      parser.next();
    }
  }
}
