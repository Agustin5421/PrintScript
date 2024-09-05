package linter.visitor.factory;

import ast.root.AstNodeType;
import java.util.Map;
import linter.visitor.LinterVisitor;
import linter.visitor.strategy.LintingStrategy;

public class LinterVisitorFactory {
  private final StrategyFactory identifierStrategyFactory;
  private final StrategyFactory callExpressionStrategyFactory;

  public LinterVisitorFactory() {
    this.identifierStrategyFactory = new IdentifierStrategyFactory();
    this.callExpressionStrategyFactory = new CallExpressionStrategyFactory();
  }

  public LinterVisitor createLinterVisitor(String rules) {
    LintingStrategy identifierLintingStrategies = getIdentifierLintingStrategies(rules);
    LintingStrategy callExpressionLintingStrategies = getCallExpressionLintingStrategies(rules);

    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(
            AstNodeType.IDENTIFIER, identifierLintingStrategies,
            AstNodeType.CALL_EXPRESSION, callExpressionLintingStrategies);

    return new LinterVisitor(nodesStrategies);
  }

  private LintingStrategy getIdentifierLintingStrategies(String rules) {
    return identifierStrategyFactory.createStrategies(rules);
  }

  private LintingStrategy getCallExpressionLintingStrategies(String rules) {
    return callExpressionStrategyFactory.createStrategies(rules);
  }
}
