package linter.visitor.strategy.callexpression;

import ast.root.AstNode;
import linter.report.FullReport;
import linter.report.Report;
import linter.visitor.strategy.LintingStrategy;

public class NoExpressionArgument implements LintingStrategy {
  @Override
  public FullReport apply(AstNode node, FullReport fullReport) {
    if (shouldApply(node)) {
      Report newReport =
          new Report(
              node.start(), node.end(), "Expressions not allowed as call expression arguments");

      return fullReport.addReport(newReport);
    }

    return fullReport;
  }

  private boolean shouldApply(AstNode node) {
    return switch (node.getType()) {
      case ASSIGNMENT_EXPRESSION, CALL_EXPRESSION, BINARY_EXPRESSION -> true;
      default -> false;
    };
  }
}