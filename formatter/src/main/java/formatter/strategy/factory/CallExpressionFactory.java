package formatter.strategy.factory;

import com.google.gson.JsonObject;
import formatter.strategy.callexpr.CallExpressionStrategy;
import formatter.strategy.callexpr.LineBreaksStrategy;
import formatter.strategy.common.ArgumentsStrategy;
import formatter.strategy.common.CallStrategy;
import formatter.strategy.common.CharacterStrategy;
import formatter.strategy.common.space.NoSpace;
import formatter.strategy.common.space.WhiteSpace;
import java.util.List;

public class CallExpressionFactory implements FormattingStrategyFactory {
  @Override
  public CallExpressionStrategy create(JsonObject rules, String version) {
    int lineBreaks = rules.get("printLineBreaks").getAsInt();
    LineBreaksStrategy lineBreaksStrategy = new LineBreaksStrategy(lineBreaks);
    NoSpace noSpace = new NoSpace();
    ArgumentsStrategy argumentsStrategy =
        new ArgumentsStrategy(List.of(noSpace, new WhiteSpace(), noSpace));
    CharacterStrategy semiColon = new CharacterStrategy(";");
    return new CallExpressionStrategy(
        new CallStrategy(lineBreaksStrategy, "", argumentsStrategy, semiColon));
  }
}
