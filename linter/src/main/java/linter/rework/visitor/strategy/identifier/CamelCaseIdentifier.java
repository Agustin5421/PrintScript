package linter.rework.visitor.strategy.identifier;

import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.root.AstNodeType;
import linter.rework.report.FullReport;
import linter.rework.report.Report;
import linter.rework.visitor.strategy.LintingStrategy;

public class CamelCaseIdentifier implements LintingStrategy {
  @Override
  public FullReport apply(AstNode node, FullReport fullReport) {
    if (!shouldApply(node)) {
      return fullReport;
    }

    Identifier identifier = (Identifier) node;

    if (!identifier.name().matches("^[a-z][a-zA-Z0-9]*$")) {
      fullReport =
          fullReport.addReport(
              new Report(
                  identifier.start(),
                  identifier.end(),
                  "Identifier " + identifier.name() + " is not in camelCase"));
    }
    return fullReport;
  }

  private boolean shouldApply(AstNode node) {
    return node.getType().equals(AstNodeType.IDENTIFIER);
  }
}
