package formatter.factory;

import ast.root.AstNodeType;
import com.google.gson.JsonObject;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.OperatorConcatenationStrategy;
import formatter.strategy.common.OperatorStrategy;
import formatter.strategy.common.WhiteSpace;
import formatter.strategy.factory.CallExpressionFactory;
import formatter.strategy.factory.FormattingStrategyFactory;
import formatter.strategy.factory.ReAssignmentFactory;
import formatter.strategy.factory.VariableDeclarationStrategyFactory;
import formatter.visitor.FormatterVisitor;
import formatter.visitor.FormatterVisitorV1;
import formatter.visitor.FormatterVisitorV2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormatterVisitorFactory {
  public static FormatterVisitor createVisitor(String version, JsonObject rules) {
    return switch (version) {
      case "1.0" -> getFormatterVisitorV1(rules);
      case "1.1" -> getFormatterVisitorV2(rules);
      default -> throw new IllegalArgumentException("Invalid version: " + version);
    };
  }

  private static FormatterVisitor getFormatterVisitorV1(JsonObject rules) {
    Map<AstNodeType, FormattingStrategy> strategies = new HashMap<>();

    FormattingStrategyFactory callExpressionFactory = new CallExpressionFactory();
    FormattingStrategy callExpressionStrategy = callExpressionFactory.create(rules);
    strategies.put(AstNodeType.CALL_EXPRESSION, callExpressionStrategy);

    OperatorConcatenationStrategy assignmentStrategy = getAssignmentStrategy(rules);

    FormattingStrategyFactory reAssignmentFactory = new ReAssignmentFactory(assignmentStrategy);
    FormattingStrategy reAssignmentStrategy = reAssignmentFactory.create(rules);
    strategies.put(AstNodeType.ASSIGNMENT_EXPRESSION, reAssignmentStrategy);

    FormattingStrategyFactory varDecStrategyFactory =
        new VariableDeclarationStrategyFactory(assignmentStrategy);
    FormattingStrategy varDecStrategy = varDecStrategyFactory.create(rules);
    strategies.put(AstNodeType.VARIABLE_DECLARATION, varDecStrategy);

    return new FormatterVisitorV1(strategies);
  }

  private static FormatterVisitor getFormatterVisitorV2(JsonObject rules) {
    return new FormatterVisitorV2(getFormatterVisitorV1(rules));
  }

  private static OperatorConcatenationStrategy getAssignmentStrategy(JsonObject rules) {
    boolean equalSpace = rules.get("equalSpaces").getAsBoolean();
    List<FormattingStrategy> strategies = new ArrayList<>();
    OperatorStrategy operatorStrategy = new OperatorStrategy("=");
    if (equalSpace) {
      WhiteSpace whiteSpace = new WhiteSpace();
      strategies.add(whiteSpace);
      strategies.add(operatorStrategy);
      strategies.add(whiteSpace);
    } else {
      strategies.add(operatorStrategy);
    }
    return new OperatorConcatenationStrategy(strategies);
  }
}
