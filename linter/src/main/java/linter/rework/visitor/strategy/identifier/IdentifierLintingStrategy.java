package linter.rework.visitor.strategy.identifier;

import ast.identifier.Identifier;
import java.util.List;
import linter.rework.report.FullReport;
import linter.rework.visitor.strategy.LintingStrategy;

public class IdentifierLintingStrategy implements LintingStrategy<Identifier> {
  private final List<LintingStrategy<Identifier>> strategies;

  public IdentifierLintingStrategy(List<LintingStrategy<Identifier>> strategies) {
    this.strategies = strategies;
  }

  @Override
  public FullReport apply(Identifier node, FullReport fullReport) {
    for (LintingStrategy<Identifier> strategy : strategies) {
      fullReport = strategy.apply(node, fullReport);
    }
    return fullReport;
  }
}
