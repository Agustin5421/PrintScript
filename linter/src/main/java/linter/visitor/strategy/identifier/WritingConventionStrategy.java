package linter.visitor.strategy.identifier;

import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.root.AstNodeType;
import linter.report.FullReport;
import linter.report.Report;
import linter.visitor.strategy.LintingStrategy;

public class WritingConventionStrategy implements LintingStrategy {
  private final String writingConventionName;
  private final String writingConventionPattern;

  public WritingConventionStrategy(String writingConventionName, String writingConventionPattern) {
    this.writingConventionName = writingConventionName;
    this.writingConventionPattern = writingConventionPattern;
  }

  @Override
  public FullReport apply(AstNode node, FullReport fullReport) {
    if (!shouldApply(node)) {
      return fullReport;
    }

    Identifier identifier = (Identifier) node;

    if (!identifier.name().matches(writingConventionPattern)) {
      fullReport =
          fullReport.addReport(
              new Report(
                  identifier.start(),
                  identifier.end(),
                  "Identifier " + identifier.name() + " is not in " + writingConventionName));
    }

    return fullReport;
  }

  private boolean shouldApply(AstNode node) {
    return node.getNodeType().equals(AstNodeType.IDENTIFIER);
  }
}
