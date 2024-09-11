package formatter.factory;

import ast.root.AstNodeType;
import com.google.gson.JsonObject;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.AssignationStrategy;
import formatter.strategy.common.CharacterStrategy;
import formatter.strategy.common.OperatorConcatenationStrategy;
import formatter.strategy.common.space.WhiteSpace;
import formatter.strategy.factory.*;
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
    Map<AstNodeType, FormattingStrategy> strategies = getAssignmentStrategies(rules, "1.0");

    FormattingStrategyFactory callExpressionFactory = new CallExpressionFactory();
    FormattingStrategy callExpressionStrategy = callExpressionFactory.create(rules, "1.0");
    strategies.put(AstNodeType.CALL_EXPRESSION, callExpressionStrategy);

    return new FormatterVisitorV1(strategies);
  }

  private static FormatterVisitor getFormatterVisitorV2(JsonObject rules) {
    FormatterVisitorV1 formatterVisitorV1 = (FormatterVisitorV1) getFormatterVisitorV1(rules);

    Map<AstNodeType, FormattingStrategy> strategies = getAssignmentStrategies(rules, "1.1");

    IfElseFactory ifElseFactory = new IfElseFactory();
    FormattingStrategy ifElseStrategy = ifElseFactory.create(rules, "1.1");
    strategies.put(AstNodeType.IF_STATEMENT, ifElseStrategy);

    int indentSize = rules.get("indentSize").getAsInt();
    return new FormatterVisitorV2(formatterVisitorV1, strategies, "", indentSize, 0);
  }

  private static Map<AstNodeType, FormattingStrategy> getAssignmentStrategies(
      JsonObject rules, String version) {
    Map<AstNodeType, FormattingStrategy> strategies = new HashMap<>();
    AssignationStrategy assignmentStrategy = getAssignmentStrategy(rules);

    FormattingStrategyFactory reAssignmentFactory = new ReAssignmentFactory(assignmentStrategy);
    FormattingStrategy reAssignmentStrategy = reAssignmentFactory.create(rules, version);
    strategies.put(AstNodeType.ASSIGNMENT_EXPRESSION, reAssignmentStrategy);

    FormattingStrategyFactory varDecStrategyFactory =
        new VariableDeclarationStrategyFactory(assignmentStrategy);
    FormattingStrategy varDecStrategy = varDecStrategyFactory.create(rules, version);
    strategies.put(AstNodeType.VARIABLE_DECLARATION, varDecStrategy);
    return strategies;
  }

  private static AssignationStrategy getAssignmentStrategy(JsonObject rules) {
    boolean equalSpace = rules.get("equalSpaces").getAsBoolean();
    List<CharacterStrategy> strategies = new ArrayList<>();
    CharacterStrategy operatorStrategy = new CharacterStrategy("=");
    if (equalSpace) {
      WhiteSpace whiteSpace = new WhiteSpace();
      strategies.add(whiteSpace);
      strategies.add(operatorStrategy);
      strategies.add(whiteSpace);
    } else {
      strategies.add(operatorStrategy);
    }
    return new AssignationStrategy(new OperatorConcatenationStrategy(strategies));
  }
}
