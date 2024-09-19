package runner;

import factory.LexerFactory;
import factory.ParserFactory;
import formatter.CodeOutput;
import formatter.MainFormatter;
import formatter.factory.FormatterFactory;
import java.util.Objects;
import lexer.Lexer;
import parsers.Parser;

public class TestRunner {
  private final Parser parser;
  private MainFormatter formatter;
  private final CodeOutput codeOutput = new CodeOutput();

  public TestRunner(String jsonOptions, String formattedCode, String version) {
    // Setting the lexer with the code;
    Lexer lexer =
        Objects.requireNonNull(LexerFactory.getLexer(version)).setInputAsString(formattedCode);
    this.parser = ParserFactory.getParser(version).setLexer(lexer);
    this.formatter = FormatterFactory.create(jsonOptions, version, codeOutput);
  }

  public String runFormatting() {
    while (parser.hasNext()) {
      formatter = formatter.formatNext(parser.next());
    }
    return codeOutput.getResult();
  }
}
