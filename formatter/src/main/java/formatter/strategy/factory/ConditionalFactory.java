package formatter.strategy.factory;

import com.google.gson.JsonObject;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.space.NoSpace;
import formatter.strategy.common.space.WhiteSpace;
import formatter.strategy.ifelse.ConditionalStatementStrategy;
import java.util.List;

public class ConditionalFactory implements FormattingStrategyFactory {
  @Override
  public FormattingStrategy create(JsonObject rules, String version) {
    NoSpace noSpace = new NoSpace();
    WhiteSpace whiteSpace = new WhiteSpace();
    return new ConditionalStatementStrategy(List.of(whiteSpace, noSpace, noSpace, whiteSpace));
  }
}
