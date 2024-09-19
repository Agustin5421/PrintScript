package linter.engine.factory;

import linter.engine.strategy.LintingStrategy;

public interface StrategyFactory {
  LintingStrategy createStrategies(String rules, String version);
}
