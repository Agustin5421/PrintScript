package linter.rework.visitor.strategy.identifier;

import ast.identifier.Identifier;
import linter.rework.report.FullReport;
import linter.rework.report.Report;
import linter.rework.visitor.strategy.LintingStrategy;

public class SnakeCaseIdentifier implements LintingStrategy<Identifier> {
  @Override
  public FullReport apply(Identifier node, FullReport fullReport) {
    if (!node.name().matches("^[a-z][a-z0-9_]*$")) {
      fullReport.addReport(
          new Report(
              node.start(), node.end(), "Identifier " + node.name() + " is not in snake_case"));
    }
    return fullReport;
  }
}
