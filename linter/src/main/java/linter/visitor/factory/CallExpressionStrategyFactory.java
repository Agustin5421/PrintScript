package linter.visitor.factory;

import ast.root.AstNodeType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.StrategiesContainer;
import linter.visitor.strategy.callexpression.ArgumentsStrategy;

public class CallExpressionStrategyFactory implements StrategyFactory {
  @Override
  public LintingStrategy createStrategies(String rules) {
    JsonObject jsonObject;

    try {
      jsonObject =
          JsonParser.parseString(rules).getAsJsonObject().getAsJsonObject("callExpression");
    } catch (Exception e) {
      return null;
    }

    LintingStrategy validArguments = getValidArguments(jsonObject);

    List<LintingStrategy> strategies = List.of(validArguments);
    return new StrategiesContainer(trimNullStrategies(strategies));
  }

  private LintingStrategy getValidArguments(JsonObject jsonObject) {
    String[] stringArgs;

    try {
      stringArgs =
          jsonObject
              .getAsJsonArray("arguments")
              .toString()
              .replace("[", "")
              .replace("]", "")
              .replace("\"", "")
              .split(",");
    } catch (Exception e) {
      return null;
    }

    List<AstNodeType> allowedArguments = new ArrayList<>();

    for (String arg : stringArgs) {
      allowedArguments.add(AstNodeType.valueOf(arg));
    }

    return new ArgumentsStrategy(allowedArguments);
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
