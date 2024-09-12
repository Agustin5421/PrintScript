package linter.visitor.factory;

import ast.root.AstNodeType;
import java.util.HashMap;
import java.util.Map;
import linter.visitor.LinterVisitor;
import linter.visitor.LinterVisitorV1;
import linter.visitor.LinterVisitorV2;
import linter.visitor.strategy.LintingStrategy;

public class LinterVisitorFactory {
  private final StrategyFactory identifierStrategyFactory;
  private final StrategyFactory callExpressionStrategyFactory;

  public LinterVisitorFactory() {
    this.identifierStrategyFactory = new IdentifierStrategyFactory();
    this.callExpressionStrategyFactory = new CallExpressionStrategyFactory();
  }

  public LinterVisitor createLinterVisitor(String rules) {
    LintingStrategy identifierLintingStrategies = getIdentifierLintingStrategies(rules, "1.0");
    LintingStrategy callExpressionLintingStrategies =
        getCallExpressionLintingStrategies(rules, "1.0");

    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(
            AstNodeType.IDENTIFIER, identifierLintingStrategies,
            AstNodeType.CALL_EXPRESSION, callExpressionLintingStrategies);

    return new LinterVisitorV1(nodesStrategies);
  }

  public LinterVisitor createLinterVisitorV2(String rules) {
    LinterVisitor visitorV1 = createLinterVisitor(rules);
    LintingStrategy callExpressionLintingStrategies =
        getCallExpressionLintingStrategies(rules, "1.1");

    Map<AstNodeType, LintingStrategy> nodesStrategies =
        new HashMap<>(visitorV1.getNodesStrategies());
    nodesStrategies.put(AstNodeType.CALL_EXPRESSION, callExpressionLintingStrategies);

    return new LinterVisitorV2(nodesStrategies, visitorV1);
  }

  private LintingStrategy getIdentifierLintingStrategies(String rules, String version) {
    return identifierStrategyFactory.createStrategies(rules, version);
  }

  private LintingStrategy getCallExpressionLintingStrategies(String rules, String version) {
    return callExpressionStrategyFactory.createStrategies(rules, version);
  }
}
