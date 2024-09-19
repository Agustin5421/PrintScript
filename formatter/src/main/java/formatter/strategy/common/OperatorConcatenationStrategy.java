package formatter.strategy.common;

import ast.root.AstNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;
import java.util.List;

public class OperatorConcatenationStrategy implements FormattingStrategy {
  // The strategies consist of whitespaces or the missing of it and
  // the operator concatenation
  private final List<CharacterStrategy> strategies;

  public OperatorConcatenationStrategy(List<CharacterStrategy> strategies) {
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
