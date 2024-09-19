package formatter.factory;

import ast.root.AstNodeType;
import com.google.gson.JsonObject;
import formatter.FormattingEngine;
import formatter.context.FormattingContext;
import formatter.context.IndentationContext;
import formatter.context.NoContext;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.AssignationStrategy;
import formatter.strategy.common.BinaryExpressionStrategy;
import formatter.strategy.common.CharacterStrategy;
import formatter.strategy.common.OperatorConcatenationStrategy;
import formatter.strategy.common.space.WhiteSpace;
import formatter.strategy.factory.*;
import formatter.strategy.word.IdentifierStrategy;
import formatter.strategy.word.literal.BooleanStrategy;
import formatter.strategy.word.literal.NumberStrategy;
import formatter.strategy.word.literal.StringStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import output.OutputResult;
import strategy.StrategyContainer;

public class EngineFactory {
  private static final String errorMessage = "Can't format";

  public static FormattingEngine createEngine(
      String version, JsonObject rules, OutputResult<String> writer) {
    return switch (version) {
      case "1.0" -> getEngineV1(rules, writer);
      case "1.1" -> getEngineV2(rules, writer);
      default -> throw new IllegalArgumentException("Invalid version: " + version);
    };
  }

  private static FormattingEngine getEngineV1(JsonObject rules, OutputResult<String> writer) {
    StrategyContainer<AstNodeType, FormattingStrategy> strategies =
        new StrategyContainer<>(getV1Strategies(rules), errorMessage);

    FormattingContext formattingContext = new NoContext();
    return new FormattingEngine(formattingContext, strategies, writer);
  }

  private static FormattingEngine getEngineV2(JsonObject rules, OutputResult<String> writer) {
    Map<AstNodeType, FormattingStrategy> strategies = getV1Strategies(rules);

    strategies.put(AstNodeType.BOOLEAN_LITERAL, new BooleanStrategy());

    IfElseFactory ifElseFactory = new IfElseFactory();
    FormattingStrategy ifElseStrategy = ifElseFactory.create(rules);
    strategies.put(AstNodeType.IF_STATEMENT, ifElseStrategy);

    int indentSize = rules.get("indentSize").getAsInt();

    StrategyContainer<AstNodeType, FormattingStrategy> strategiesContainer =
        new StrategyContainer<>(strategies, errorMessage);
    FormattingContext formattingContext = new IndentationContext(indentSize, 0);

    return new FormattingEngine(formattingContext, strategiesContainer, writer);
  }

  private static Map<AstNodeType, FormattingStrategy> getV1Strategies(JsonObject rules) {
    Map<AstNodeType, FormattingStrategy> strategies = getAssignmentStrategies(rules);

    FormattingStrategyFactory callExpressionFactory = new CallExpressionFactory();
    FormattingStrategy callExpressionStrategy = callExpressionFactory.create(rules);
    strategies.put(AstNodeType.CALL_EXPRESSION, callExpressionStrategy);

    WhiteSpace whiteSpace = new WhiteSpace();
    strategies.put(
        AstNodeType.BINARY_EXPRESSION, new BinaryExpressionStrategy(whiteSpace, whiteSpace));
    strategies.put(AstNodeType.NUMBER_LITERAL, new NumberStrategy());
    strategies.put(AstNodeType.STRING_LITERAL, new StringStrategy());
    strategies.put(AstNodeType.IDENTIFIER, new IdentifierStrategy());

    return strategies;
  }

  private static Map<AstNodeType, FormattingStrategy> getAssignmentStrategies(JsonObject rules) {
    Map<AstNodeType, FormattingStrategy> strategies = new HashMap<>();
    AssignationStrategy assignmentStrategy = getAssignmentStrategy(rules);

    FormattingStrategyFactory reAssignmentFactory = new ReAssignmentFactory(assignmentStrategy);
    FormattingStrategy reAssignmentStrategy = reAssignmentFactory.create(rules);
    strategies.put(AstNodeType.ASSIGNMENT_EXPRESSION, reAssignmentStrategy);

    FormattingStrategyFactory varDecStrategyFactory =
        new VariableDeclarationStrategyFactory(assignmentStrategy);
    FormattingStrategy varDecStrategy = varDecStrategyFactory.create(rules);
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
