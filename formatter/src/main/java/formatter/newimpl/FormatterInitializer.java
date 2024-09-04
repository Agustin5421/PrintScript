package formatter.newimpl;

import com.google.gson.JsonObject;
import formatter.OptionsChecker;
import formatter.newimpl.strategy.WhiteSpace;
import java.util.HashMap;
import java.util.List;
import observers.Observer;
import observers.ProgressObserver;
import observers.ProgressPrinter;

public class FormatterInitializer {
  public static MainFormatter2 init(String options) {
    return init(new ProgressObserver(new ProgressPrinter(), 1), options);
  }

  public static MainFormatter2 init(Observer observer, String options) {
    JsonObject rules = OptionsChecker.checkAndReturn(options);
    return new MainFormatter2(List.of(observer), createVisitor(rules));
  }

  private static FormatterVisitor2 createVisitor(JsonObject rules) {
    WhiteSpace whiteSpace = new WhiteSpace();
    return new FormatterVisitor2(new HashMap<>());
  }
}
