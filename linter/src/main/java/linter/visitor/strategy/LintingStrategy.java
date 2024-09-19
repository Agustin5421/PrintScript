package linter.visitor.strategy;

import ast.root.AstNode;
import linter.visitor.report.FullReport;
import strategy.Strategy;

public interface LintingStrategy extends Strategy<NewLinterVisitor> {
  FullReport oldApply(AstNode node, FullReport fullReport);
}
