package formatter.strategy.factory;

import com.google.gson.JsonObject;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.AssignationStrategy;
import formatter.strategy.common.CharacterStrategy;
import formatter.strategy.common.space.WhiteSpace;
import formatter.strategy.vardec.GetTypeStrategy;
import formatter.strategy.vardec.TypingStrategy;
import formatter.strategy.vardec.VariableDeclarationStrategy;
import java.util.ArrayList;
import java.util.List;

public class VariableDeclarationStrategyFactory implements FormattingStrategyFactory {
  private final AssignationStrategy assignationStrategy;

  public VariableDeclarationStrategyFactory(AssignationStrategy assignationStrategy) {
    this.assignationStrategy = assignationStrategy;
  }

  @Override
  public FormattingStrategy create(JsonObject rules, String version) {
    List<FormattingStrategy> strategies = new ArrayList<>();
    WhiteSpace whiteSpace = new WhiteSpace();
    JsonObject colonRules = rules.getAsJsonObject("colonRules");
    boolean beforeSpace = colonRules.get("before").getAsBoolean();
    boolean afterSpace = colonRules.get("after").getAsBoolean();
    if (beforeSpace) {
      strategies.add(whiteSpace);
    }
    strategies.add(new CharacterStrategy(":"));
    if (afterSpace) {
      strategies.add(whiteSpace);
    }
    strategies.add(new GetTypeStrategy());
    TypingStrategy semiColonStrategy = new TypingStrategy(strategies);
    return new VariableDeclarationStrategy(semiColonStrategy, " ", assignationStrategy);
  }
}
