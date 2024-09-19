package linter.engine.strategy.identifier;

import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.root.AstNodeType;
import linter.engine.LinterEngine;
import linter.engine.report.FullReport;
import linter.engine.report.Report;
import linter.engine.strategy.LintingStrategy;

public class WritingConventionStrategy implements LintingStrategy {
  private final String writingConventionName;
  private final String writingConventionPattern;

  public WritingConventionStrategy(String writingConventionName, String writingConventionPattern) {
    this.writingConventionName = writingConventionName;
    this.writingConventionPattern = writingConventionPattern;
  }

  @Override
  public FullReport oldApply(AstNode node, FullReport fullReport) {
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

  @Override
  public LinterEngine apply(AstNode node, LinterEngine engine) {
    if (!shouldApply(node)) {
      return engine;
    }

    Identifier identifier = (Identifier) node;

    if (!identifier.name().matches(writingConventionPattern)) {
      Report newReport =
          new Report(
              identifier.start(),
              identifier.end(),
              "Identifier " + identifier.name() + " is not in " + writingConventionName);

      engine.getOutput().saveResult(newReport.toString());
    }

    return engine;
  }

  private boolean shouldApply(AstNode node) {
    return node.getNodeType().equals(AstNodeType.IDENTIFIER);
  }
}
