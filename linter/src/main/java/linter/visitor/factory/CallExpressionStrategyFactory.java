package linter.visitor.factory;

import ast.root.AstNodeType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.StrategiesContainer;
import linter.visitor.strategy.callexpression.ArgumentsStrategy;
import linter.visitor.strategy.callexpression.MethodArgumentsStrategy;

public class CallExpressionStrategyFactory implements StrategyFactory {
  @Override
  public LintingStrategy createStrategies(String rules, String version) {
    JsonObject jsonObject =
        JsonParser.parseString(rules).getAsJsonObject().getAsJsonObject("callExpression");

    if (jsonObject == null) {
      return new StrategiesContainer(List.of());
    }
    return switch (version) {
      case "1.0" -> new StrategiesContainer(getStrategiesV1(jsonObject));
      case "1.1" -> new StrategiesContainer(getStrategiesV2(jsonObject));
      default -> throw new IllegalArgumentException("Invalid version: " + version);
    };
  }

  public List<LintingStrategy> getStrategiesV1(JsonObject jsonObject) {
    List<LintingStrategy> strategies = new ArrayList<>();

    try {
      JsonObject printlnArguments = jsonObject.getAsJsonObject("println");
      strategies.add(new MethodArgumentsStrategy("println", getValidArguments(printlnArguments)));

      return trimNullStrategies(strategies);
    } catch (Exception e) {
      return strategies;
    }
  }

  public List<LintingStrategy> getStrategiesV2(JsonObject jsonObject) {
    List<LintingStrategy> strategies = getStrategiesV1(jsonObject);

    try {
      JsonObject readInputArguments = jsonObject.getAsJsonObject("readInput");
      strategies.add(
          new MethodArgumentsStrategy("readInput", getValidArguments(readInputArguments)));

      return trimNullStrategies(strategies);
    } catch (Exception e) {
      return strategies;
    }
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
