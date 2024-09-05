package formatter.newimpl.strategy.factory;

import com.google.gson.JsonObject;
import formatter.newimpl.strategy.*;
import java.util.List;

public class CallExpressionFactory implements FormattingStrategyFactory {
  @Override
  public CallExpressionStrategy create(JsonObject rules) {
    int lineBreaks = rules.get("printLineBreaks").getAsInt();
    LineBreaksStrategy lineBreaksStrategy = new LineBreaksStrategy(lineBreaks);
    return new CallExpressionStrategy(
        List.of(lineBreaksStrategy, new ArgumentsStrategy(new WhiteSpace())));
  }
}
