package formatter.strategy.factory;

import com.google.gson.JsonObject;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.OperatorStrategy;
import formatter.strategy.common.WhiteSpace;
import formatter.strategy.vardec.GetTypeStrategy;
import formatter.strategy.vardec.TypingStrategy;
import formatter.strategy.vardec.VariableDeclarationStrategy;
import java.util.ArrayList;
import java.util.List;

public class VariableDeclarationStrategyFactory implements FormattingStrategyFactory {
  private final FormattingStrategy equalStrategy;

  public VariableDeclarationStrategyFactory(FormattingStrategy equalStrategy) {
    this.equalStrategy = equalStrategy;
  }

  @Override
  public FormattingStrategy create(JsonObject rules) {
    List<FormattingStrategy> strategies = new ArrayList<>();
    WhiteSpace whiteSpace = new WhiteSpace();
    JsonObject colonRules = rules.getAsJsonObject("colonRules");
    boolean beforeSpace = colonRules.get("before").getAsBoolean();
    boolean afterSpace = colonRules.get("after").getAsBoolean();
    if (beforeSpace) {
      strategies.add(whiteSpace);
    }
    strategies.add(new OperatorStrategy(":"));
    if (afterSpace) {
      strategies.add(whiteSpace);
    }
    strategies.add(new GetTypeStrategy());
    TypingStrategy semiColonStrategy = new TypingStrategy(strategies);
    return new VariableDeclarationStrategy(List.of(semiColonStrategy, equalStrategy), "let");
  }
}