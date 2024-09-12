package formatter.factory;

import com.google.gson.JsonObject;
import factory.ParserFactory;
import formatter.MainFormatter;
import formatter.OptionsChecker;
import java.util.List;
import observers.Observer;
import observers.ProgressObserver;
import observers.ProgressPrinter;
import parsers.Parser;

public class FormatterInitializer {
  public static MainFormatter init(String options, String code, String version, Observer observer) {
    JsonObject rules = OptionsChecker.checkAndReturn(options, version);
    Parser parser = ParserFactory.getParser(version);
    parser = parser.setLexer(parser.getLexer().setInputAsString(code));
    return new MainFormatter(
        List.of(observer), FormatterVisitorFactory.createVisitor(version, rules), parser);
  }

  public static MainFormatter init(String options, String code, String version) {
    return init(options, code, version, new ProgressObserver(new ProgressPrinter(), 1));
  }
}
