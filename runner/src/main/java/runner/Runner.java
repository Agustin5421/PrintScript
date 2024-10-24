package runner;

import factory.LexerFactory;
import factory.ParserFactory;
import formatter.CodeOutput;
import formatter.MainFormatter;
import formatter.factory.FormatterFactory;
import input.InputHandler;
import interpreter.Interpreter;
import interpreter.InterpreterFactory;
import interpreter.engine.staticprovider.Inputs;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import lexer.Lexer;
import linter.Linter;
import linter.LinterFactory;
import observers.ProgressObserver;
import output.OutputResult;
import parsers.Parser;
import report.Report;

public class Runner {
  private final ProgressObserver progressObserver;

  public Runner(ProgressObserver progressObserver) {
    this.progressObserver = progressObserver;
  }

  public Runner() {
    this.progressObserver = null;
  }

  public void execute(
      InputStream code,
      String version,
      OutputResult<String> printLog,
      OutputResult<String> errorLog,
      InputHandler inputs) {
    Lexer lexer;
    try {
      lexer =
          Objects.requireNonNull(LexerFactory.getLexer(version))
              .setInputWithObserver(code, progressObserver);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    Parser parser = ParserFactory.getParser(version).setLexer(lexer);
    Interpreter interpreter = InterpreterFactory.buildInterpreter(version, printLog);

    Inputs.setInputs(inputs);

    try {
      while (parser.hasNext()) {
        interpreter = interpreter.interpretNext(parser.next());
      }
    } catch (Throwable e) {
      lexer = null;
      parser = null;
      interpreter = null;
      System.gc();
      errorLog.saveResult(e.getMessage());
    }
  }

  public void analyze(
      InputStream code, String version, String config, OutputResult<Report> output) {
    Lexer lexer;
    try {
      lexer = Objects.requireNonNull(LexerFactory.getLexer(version)).setInput(code);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    Parser parser = ParserFactory.getParser(version).setLexer(lexer);
    Linter linter = LinterFactory.getReworkedLinter(version, config, output);

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }
  }

  public void format(InputStream code, String version, String config, OutputResult<String> output) throws IOException {
    Lexer lexer = Objects.requireNonNull(LexerFactory.getLexer(version)).setInput(code);
    Parser parser = ParserFactory.getParser(version).setLexer(lexer);
    MainFormatter formatter = FormatterFactory.create(config, version, output);

    while (parser.hasNext()) {
      formatter = formatter.formatNext(parser.next());
    }
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
