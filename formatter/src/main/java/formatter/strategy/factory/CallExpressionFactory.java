package formatter.strategy.factory;

import com.google.gson.JsonObject;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.callexpr.CallExpressionStrategy;
import formatter.strategy.callexpr.LineBreaksStrategy;
import formatter.strategy.common.ArgumentsStrategy;
import formatter.strategy.common.CallStrategy;
import formatter.strategy.common.space.NoSpace;
import formatter.strategy.common.space.WhiteSpace;
import java.util.List;

public class CallExpressionFactory implements FormattingStrategyFactory {
  @Override
  public FormattingStrategy create(JsonObject rules) {
    int lineBreaks = rules.get("printLineBreaks").getAsInt();
    LineBreaksStrategy lineBreaksStrategy = new LineBreaksStrategy(lineBreaks);
    NoSpace noSpace = new NoSpace();
    ArgumentsStrategy argumentsStrategy =
        new ArgumentsStrategy(List.of(noSpace, new WhiteSpace(), noSpace));
    return new CallExpressionStrategy(
        new CallStrategy(lineBreaksStrategy, "", argumentsStrategy, noSpace));
  }
}
