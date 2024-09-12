package linter.visitor.factory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.StrategiesContainer;
import linter.visitor.strategy.identifier.WritingConventionStrategy;

public class IdentifierStrategyFactory implements StrategyFactory {
  @Override
  public LintingStrategy createStrategies(String rules, String version) {
    JsonObject jsonObject =
        JsonParser.parseString(rules).getAsJsonObject().getAsJsonObject("identifier");

    if (jsonObject == null) {
      return new StrategiesContainer(List.of());
    }

    List<LintingStrategy> strategies = new ArrayList<>();

    LintingStrategy identifierWritingConvention = getIdentifierWritingConvention(jsonObject);

    strategies.add(identifierWritingConvention);
    return new StrategiesContainer(trimNullStrategies(strategies));
  }

  private LintingStrategy getIdentifierWritingConvention(JsonObject jsonObject) {
    try {
      JsonObject convention = jsonObject.get("writingConvention").getAsJsonObject();
      String conventionName = convention.get("conventionName").getAsString();
      String conventionPattern = convention.get("conventionPattern").getAsString();

      return new WritingConventionStrategy(conventionName, conventionPattern);
    } catch (Exception e) {
      return null;
    }
  }

  private List<LintingStrategy> trimNullStrategies(List<LintingStrategy> strategies) {
    List<LintingStrategy> trimmedStrategies = new ArrayList<>();
    for (LintingStrategy strategy : strategies) {
      if (strategy != null) {
        trimmedStrategies.add(strategy);
      }
    }
    return trimmedStrategies;
  }
}
