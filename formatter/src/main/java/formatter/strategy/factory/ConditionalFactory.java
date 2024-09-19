package formatter.strategy.factory;

import com.google.gson.JsonObject;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.ArgumentsStrategy;
import formatter.strategy.common.CallStrategy;
import formatter.strategy.common.CharacterStrategy;
import formatter.strategy.common.space.NoSpace;
import formatter.strategy.common.space.WhiteSpace;
import formatter.strategy.ifelse.ConditionalStatementStrategy;
import java.util.List;

public class ConditionalFactory implements FormattingStrategyFactory {
  @Override
  public FormattingStrategy create(JsonObject rules) {
    NoSpace noSpace = new NoSpace();
    CharacterStrategy whiteSpace = new WhiteSpace();
    ArgumentsStrategy argumentsStrategy =
        new ArgumentsStrategy(List.of(noSpace, whiteSpace, noSpace));
    return new ConditionalStatementStrategy(
        new CallStrategy(noSpace, " ", argumentsStrategy, whiteSpace));
  }
}
