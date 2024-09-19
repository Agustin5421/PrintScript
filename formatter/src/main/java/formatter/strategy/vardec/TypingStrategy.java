package formatter.strategy.vardec;

import ast.root.AstNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;
import java.util.List;

public class TypingStrategy implements FormattingStrategy {
  private final List<FormattingStrategy> strategies;

  public TypingStrategy(List<FormattingStrategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    for (FormattingStrategy strategy : strategies) {
      strategy.apply(node, engine);
    }
    return engine;
  }
}
