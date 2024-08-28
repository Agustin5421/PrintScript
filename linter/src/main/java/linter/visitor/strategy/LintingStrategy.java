package linter.visitor.strategy;

import ast.root.AstNode;
import linter.report.FullReport;

public interface LintingStrategy {
  FullReport apply(AstNode node, FullReport fullReport);
}
