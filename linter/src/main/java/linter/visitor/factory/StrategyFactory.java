package linter.visitor.factory;

import linter.visitor.strategy.LintingStrategy;

public interface StrategyFactory {
  LintingStrategy createStrategies(String rules);
}
