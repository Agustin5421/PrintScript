package linter.rework.visitor.strategy.callexpression;

import ast.root.AstNode;
import linter.rework.report.FullReport;
import linter.rework.report.Report;
import linter.rework.visitor.strategy.LintingStrategy;

public class NoLiteralArgument implements LintingStrategy {
  @Override
  public FullReport apply(AstNode node, FullReport fullReport) {
    if (shouldApply(node)) {
      Report newReport =
          new Report(node.start(), node.end(), "Literals not allowed as call expression arguments");

      return fullReport.addReport(newReport);
    }

    return fullReport;
  }

  @Override
  public boolean shouldApply(AstNode node) {
    return switch (node.getType()) {
      case STRING_LITERAL, NUMBER_LITERAL -> true;
      default -> false;
    };
  }
}
