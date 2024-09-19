package formatter.factory;

import com.google.gson.JsonObject;
import factory.ParserFactory;
import formatter.MainFormatter;
import formatter.OptionsChecker;
import parsers.Parser;

public class FormatterInitializer {
  public static MainFormatter init(String options, String code, String version) {
    JsonObject rules = OptionsChecker.checkAndReturn(options, version);
    Parser parser = ParserFactory.getParser(version);
    parser = parser.setLexer(parser.getLexer().setInputAsString(code));
    return new MainFormatter(FormatterVisitorFactory.createVisitor(version, rules), parser);
  }
}
