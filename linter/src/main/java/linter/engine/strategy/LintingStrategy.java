package linter.engine.strategy;

import ast.root.AstNode;
import linter.engine.LinterEngine;
import report.FullReport;
import strategy.Strategy;

public interface LintingStrategy extends Strategy<LinterEngine> {
  FullReport oldApply(AstNode node, FullReport fullReport);
}
