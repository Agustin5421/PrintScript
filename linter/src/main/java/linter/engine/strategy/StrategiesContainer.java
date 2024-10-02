package linter.engine.strategy;

import ast.root.AstNode;
import java.util.List;
import linter.engine.LinterEngine;

public class StrategiesContainer implements LintingStrategy {
  private final List<LintingStrategy> strategies;

  public StrategiesContainer(List<LintingStrategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public LinterEngine apply(AstNode node, LinterEngine engine) {
    for (LintingStrategy strategy : strategies) {
      engine = strategy.apply(node, engine);
    }

    return engine;
  }
}
