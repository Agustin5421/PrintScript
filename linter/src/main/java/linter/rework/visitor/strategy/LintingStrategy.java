package linter.rework.visitor.strategy;

import ast.root.AstNode;
import linter.rework.report.FullReport;

public interface LintingStrategy {
  FullReport apply(AstNode node, FullReport fullReport);
}
