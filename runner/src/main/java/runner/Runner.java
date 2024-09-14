package runner;

import factory.LexerFactory;
import factory.ParserFactory;
import interpreter.Interpreter;
import interpreter.factory.InterpreterFactory;
import java.util.List;
import lexer.Lexer;
import linter.Linter;
import linter.LinterFactory;
import observers.ProgressObserver;
import output.OutputResult;
import parsers.Parser;

public class Runner {
  private final ProgressObserver progressObserver;

    public Runner(ProgressObserver progressObserver) {
        this.progressObserver = progressObserver;
    }

    public Runner() {
      this.progressObserver = null;
    }

    public void execute(String code, String version, OutputResult printLog, OutputResult errorLog) {
    Interpreter interpreter = InterpreterFactory.getInterpreter(version);

    try {
      List<String> prints = interpreter.interpret(code);
      for (String print : prints) {
        printLog.saveResult(print);
      }
    } catch (Exception e) {
      errorLog.saveResult(e.getMessage());
    }
  }

  public void analyze(String code, String version, String config, OutputResult output) {
    Linter linter = LinterFactory.getLinter(version, config);
    linter = linter.setInput(code);

    while (linter.hasNext()) {
      output.saveResult(linter.next().toString());
    }
  }

  public void format(String code, String version, String config) {
    // TODO:
  }

  public void validate(String input, String version) {
    Lexer lexer = LexerFactory.getLexer(version);
    Parser parser = ParserFactory.getParser(version);

    assert lexer != null;
    parser = parser.setLexer(lexer.setInputAsString(input));

    while (parser.hasNext()) {
      parser.next();
    }
  }
}
