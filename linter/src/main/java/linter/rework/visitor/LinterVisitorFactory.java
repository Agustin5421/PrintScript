package linter.rework.visitor;

import ast.root.AstNodeType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import linter.rework.visitor.strategy.LintingStrategy;
import linter.rework.visitor.strategy.callexpression.CallExpressionLintingStrategy;
import linter.rework.visitor.strategy.callexpression.NoExpressionArgument;
import linter.rework.visitor.strategy.callexpression.NoLiteralArgument;
import linter.rework.visitor.strategy.identifier.CamelCaseIdentifier;
import linter.rework.visitor.strategy.identifier.IdentifierLintingStrategy;
import linter.rework.visitor.strategy.identifier.SnakeCaseIdentifier;

public class LinterVisitorFactory {
  public LinterVisitorV2 createLinterVisitor(String rules) {
    LintingStrategy identifierLintingStrategies = getIdentifierLintingStrategies(rules);
    LintingStrategy callExpressionLintingStrategies = getCallExpressionLintingStrategies(rules);

    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(
            AstNodeType.IDENTIFIER, identifierLintingStrategies,
            AstNodeType.CALL_EXPRESSION, callExpressionLintingStrategies);

    return new LinterVisitorV2(nodesStrategies);
  }

  private LintingStrategy getIdentifierLintingStrategies(String rules) {
    JsonObject jsonObject =
        JsonParser.parseString(rules).getAsJsonObject().getAsJsonObject("identifier");

    List<LintingStrategy> identifierStrategies = getIdentifierWritingConventions(jsonObject);

    return new IdentifierLintingStrategy(identifierStrategies);
  }

  private List<LintingStrategy> getIdentifierWritingConventions(JsonObject jsonObject) {
    String convention = jsonObject.get("writingConvention").getAsString();

    List<LintingStrategy> strategies = new ArrayList<>();

    switch (convention) {
      case "camelCase":
        strategies.add(new CamelCaseIdentifier());
        break;
      case "snakeCase":
        strategies.add(new SnakeCaseIdentifier());
        break;
      default:
        break;
    }

    return strategies;
  }

  private LintingStrategy getCallExpressionLintingStrategies(String rules) {
    JsonObject jsonObject =
        JsonParser.parseString(rules).getAsJsonObject().getAsJsonObject("callExpression");

    List<LintingStrategy> callExpressionStrategies = getArgumentsStrategies(jsonObject);

    return new CallExpressionLintingStrategy(callExpressionStrategies);
  }

  private List<LintingStrategy> getArgumentsStrategies(JsonObject jsonObject) {
    boolean acceptExpressions = jsonObject.get("acceptExpressions").getAsBoolean();
    boolean acceptLiterals = jsonObject.get("acceptLiterals").getAsBoolean();

    List<LintingStrategy> strategies = new ArrayList<>();

    if (!acceptExpressions) {
      strategies.add(new NoExpressionArgument());
    }
    if (!acceptLiterals) {
      strategies.add(new NoLiteralArgument());
    }

    return strategies;
  }
}
