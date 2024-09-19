package formatter.strategy.factory;

import com.google.gson.JsonObject;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.space.WhiteSpace;
import formatter.strategy.ifelse.ConditionalStatementStrategy;
import formatter.strategy.ifelse.IfElseStrategy;
import java.util.List;

public class IfElseFactory implements FormattingStrategyFactory {
  @Override
  public FormattingStrategy create(JsonObject rules) {
    ConditionalFactory conditionalFactory = new ConditionalFactory();
    ConditionalStatementStrategy conditionalStatementStrategy =
        (ConditionalStatementStrategy) conditionalFactory.create(rules);
    WhiteSpace whiteSpace = new WhiteSpace();
    return new IfElseStrategy(conditionalStatementStrategy, List.of(whiteSpace, whiteSpace));
  }
}
