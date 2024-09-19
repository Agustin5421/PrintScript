package formatter.strategy.common;

import ast.root.AstNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;

public class AssignationStrategy implements FormattingStrategy {
  // Strategy for adding the assignation operator
  private final OperatorConcatenationStrategy strategy;

  public AssignationStrategy(OperatorConcatenationStrategy strategy) {
    this.strategy = strategy;
  }

  // We receive the expression as the node
  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    strategy.apply(node, engine);
    return engine.format(node);
  }
}
