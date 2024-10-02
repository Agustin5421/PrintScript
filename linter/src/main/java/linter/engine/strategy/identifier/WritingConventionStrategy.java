package linter.engine.strategy.identifier;

import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.root.AstNodeType;
import linter.engine.LinterEngine;
import linter.engine.strategy.LintingStrategy;
import report.Report;

public class WritingConventionStrategy implements LintingStrategy {
  private final String writingConventionName;
  private final String writingConventionPattern;

  public WritingConventionStrategy(String writingConventionName, String writingConventionPattern) {
    this.writingConventionName = writingConventionName;
    this.writingConventionPattern = writingConventionPattern;
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

      engine.getOutput().saveResult(newReport);
    }

    return engine;
  }

  private boolean shouldApply(AstNode node) {
    return node.getNodeType().equals(AstNodeType.IDENTIFIER);
  }
}
