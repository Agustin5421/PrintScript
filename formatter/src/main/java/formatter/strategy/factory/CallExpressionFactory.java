package formatter.strategy.factory;

import com.google.gson.JsonObject;
import formatter.strategy.callexpr.ArgumentsStrategy;
import formatter.strategy.callexpr.CallExpressionStrategy;
import formatter.strategy.callexpr.LineBreaksStrategy;
import formatter.strategy.common.space.WhiteSpace;
import java.util.List;

public class CallExpressionFactory implements FormattingStrategyFactory {
  @Override
  public CallExpressionStrategy create(JsonObject rules, String version) {
    int lineBreaks = rules.get("printLineBreaks").getAsInt();
    LineBreaksStrategy lineBreaksStrategy = new LineBreaksStrategy(lineBreaks);
    return new CallExpressionStrategy(
        List.of(lineBreaksStrategy, new ArgumentsStrategy(new WhiteSpace())));
  }
}
