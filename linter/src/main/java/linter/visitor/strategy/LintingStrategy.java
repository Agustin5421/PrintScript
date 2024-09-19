package linter.visitor.strategy;

import ast.root.AstNode;
import linter.visitor.LinterVisitor;
import linter.visitor.report.FullReport;
import strategy.Strategy;

public interface LintingStrategy extends Strategy<LinterVisitor> {
  FullReport apply(AstNode node, FullReport fullReport);
}
