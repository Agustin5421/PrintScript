package formatter.strategy.factory;

import com.google.gson.JsonObject;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.space.WhiteSpace;
import formatter.strategy.ifelse.ConditionalStatementStrategy;
import formatter.strategy.ifelse.IfElseStrategy;
import formatter.strategy.ifelse.IndentStrategy;
import java.util.List;

public class IfElseFactory implements FormattingStrategyFactory {
  @Override
  public FormattingStrategy create(JsonObject rules, String version) {
    ConditionalFactory conditionalFactory = new ConditionalFactory();
    ConditionalStatementStrategy conditionalStatementStrategy =
        (ConditionalStatementStrategy) conditionalFactory.create(rules, "1.1");
    WhiteSpace whiteSpace = new WhiteSpace();
    IndentStrategy indentStrategy = new IndentStrategy();
    return new IfElseStrategy(
        conditionalStatementStrategy, List.of(whiteSpace, whiteSpace), indentStrategy);
  }
}
