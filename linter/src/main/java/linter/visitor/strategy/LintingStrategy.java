package linter.visitor.strategy;

import ast.root.AstNode;
import linter.visitor.report.FullReport;

public interface LintingStrategy {
  FullReport apply(AstNode node, FullReport fullReport);
}
