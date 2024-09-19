package linter.visitor.strategy;

import ast.root.AstNode;
import java.util.List;
import linter.visitor.LinterVisitor;
import linter.visitor.report.FullReport;

public class StrategiesContainer implements LintingStrategy {
  private final List<LintingStrategy> strategies;

  public StrategiesContainer(List<LintingStrategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public FullReport oldApply(AstNode node, FullReport fullReport) {
    for (LintingStrategy strategy : strategies) {
      fullReport = strategy.oldApply(node, fullReport);
    }

    return fullReport;
  }

  @Override
  public LinterVisitor apply(AstNode node, LinterVisitor visitor) {
    for (LintingStrategy strategy : strategies) {
      visitor = strategy.apply(node, visitor);
    }

    return visitor;
  }
}
