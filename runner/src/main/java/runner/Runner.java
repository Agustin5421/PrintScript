package runner;

import factory.LexerFactory;
import factory.ParserFactory;
import lexer.Lexer;
import linter.Linter;
import linter.LinterFactory;
import parsers.Parser;

public class Runner {
  public void execute(String code, String version) {
    // TODO: need factory for executor

  }

  public void analyze(String code, String version, String config, OutputResult output) {
    Linter linter = LinterFactory.getLinter(version, config);
    linter = linter.setInput(code);

    while (linter.hasNext()) {
      output.saveResult(linter.next().toString());
    }
  }

  public void format(String code, String version, String config) {
    // TODO: need factory for formatter

  }

  public void validate(String input, String version) {
    // TODO: lexer should receive codeFilePath
    Lexer lexer = LexerFactory.getLexer(version);
    Parser parser = ParserFactory.getParser(version);

      assert lexer != null;
      parser = parser.setLexer(lexer.setInputAsString(input));
  }
}
