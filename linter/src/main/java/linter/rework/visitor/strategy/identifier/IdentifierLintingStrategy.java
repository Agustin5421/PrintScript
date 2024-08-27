package linter.rework.visitor.strategy.identifier;

import ast.root.AstNode;
import ast.root.AstNodeType;
import java.util.List;
import linter.rework.report.FullReport;
import linter.rework.visitor.strategy.LintingStrategy;

public class IdentifierLintingStrategy implements LintingStrategy {
  private final List<LintingStrategy> strategies;

  public IdentifierLintingStrategy(List<LintingStrategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public FullReport apply(AstNode node, FullReport fullReport) {
    for (LintingStrategy strategy : strategies) {
      fullReport = strategy.apply(node, fullReport);
    }
    return fullReport;
  }

  @Override
  public boolean shouldApply(AstNode node) {
    return node.getType().equals(AstNodeType.IDENTIFIER);
  }
}
