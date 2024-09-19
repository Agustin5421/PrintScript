package linter.engine.factory;

import ast.root.AstNodeType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import linter.engine.LinterEngine;
import linter.engine.strategy.LintingStrategy;
import linter.engine.strategy.StrategiesContainer;
import linter.engine.strategy.assign.AssignmentExpressionTraversing;
import linter.engine.strategy.binary.BinaryExpressionTraversing;
import linter.engine.strategy.callexpression.CallExpressionTraversing;
import linter.engine.strategy.conditional.IfTraversing;
import linter.engine.strategy.vardec.VariableDeclarationTraversing;
import output.OutputResult;
import strategy.StrategyContainer;

public class LinterEngineFactory {
  private final StrategyFactory identifierStrategyFactory;
  private final StrategyFactory callExpressionStrategyFactory;

  public LinterEngineFactory() {
    this.identifierStrategyFactory = new IdentifierStrategyFactory();
    this.callExpressionStrategyFactory = new CallExpressionStrategyFactory();
  }

  public LinterEngine createLinterEngine(
      String version, String rules, OutputResult<String> output) {
    LintingStrategy identifierLintingStrategies = getIdentifierLintingStrategies(rules, version);
    LintingStrategy callExpressionLintingStrategies =
        getCallExpressionLintingStrategies(rules, version);

    StrategiesContainer mockStrategy = new StrategiesContainer(List.of());
    Map<AstNodeType, LintingStrategy> nodesStrategies = getCommonStandByStrategies(mockStrategy);

    nodesStrategies.put(AstNodeType.IDENTIFIER, identifierLintingStrategies);
    nodesStrategies.put(
        AstNodeType.CALL_EXPRESSION, new CallExpressionTraversing(callExpressionLintingStrategies));

    if (version.equals("1.1")) {
      nodesStrategies.put(AstNodeType.IF_STATEMENT, new IfTraversing(mockStrategy));
      nodesStrategies.put(AstNodeType.BOOLEAN_LITERAL, mockStrategy);
    }

    return new LinterEngine(new StrategyContainer<>(nodesStrategies, "Can't lint: "), output);
  }

  private LintingStrategy getIdentifierLintingStrategies(String rules, String version) {
    return identifierStrategyFactory.createStrategies(rules, version);
  }

  private LintingStrategy getCallExpressionLintingStrategies(String rules, String version) {
    return callExpressionStrategyFactory.createStrategies(rules, version);
  }

  private Map<AstNodeType, LintingStrategy> getCommonStandByStrategies(
      StrategiesContainer mockStrategy) {
    return new HashMap<>(
        Map.of(
            AstNodeType.VARIABLE_DECLARATION, new VariableDeclarationTraversing(mockStrategy),
            AstNodeType.ASSIGNMENT_EXPRESSION, new AssignmentExpressionTraversing(mockStrategy),
            AstNodeType.BINARY_EXPRESSION, new BinaryExpressionTraversing(mockStrategy),
            AstNodeType.NUMBER_LITERAL, mockStrategy,
            AstNodeType.STRING_LITERAL, mockStrategy));
  }
}
