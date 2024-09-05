package linter.visitor.strategy;

import ast.root.AstNode;
import java.util.List;
import linter.report.FullReport;

public class StrategiesContainer implements LintingStrategy {
  private final List<LintingStrategy> strategies;

  public StrategiesContainer(List<LintingStrategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public FullReport apply(AstNode node, FullReport fullReport) {
    for (LintingStrategy strategy : strategies) {
      fullReport = strategy.apply(node, fullReport);
    }

    return fullReport;
  }
}