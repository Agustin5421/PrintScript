package linter.visitor.strategy.identifier;

import ast.root.AstNode;
import ast.root.AstNodeType;
import java.util.List;
import linter.report.FullReport;
import linter.visitor.strategy.LintingStrategy;

public class IdentifierLintingStrategy implements LintingStrategy {
  private final List<LintingStrategy> strategies;

  public IdentifierLintingStrategy(List<LintingStrategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public FullReport apply(AstNode node, FullReport fullReport) {
    if (!shouldApply(node)) {
      return fullReport;
    }

    for (LintingStrategy strategy : strategies) {
      fullReport = strategy.apply(node, fullReport);
    }
    return fullReport;
  }

  private boolean shouldApply(AstNode node) {
    return node.getNodeType().equals(AstNodeType.IDENTIFIER);
  }
}
