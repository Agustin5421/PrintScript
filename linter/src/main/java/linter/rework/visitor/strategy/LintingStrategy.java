package linter.rework.visitor.strategy;

import linter.rework.report.FullReport;

public interface LintingStrategy<T> {
  FullReport apply(T node, FullReport fullReport);
}
