package formatter.strategy.common;

import ast.root.AstNode;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitor;

public class AssignationStrategy implements FormattingStrategy {
  // Strategy for adding the assignation operator
  private final OperatorConcatenationStrategy strategy;

  public AssignationStrategy(OperatorConcatenationStrategy strategy) {
    this.strategy = strategy;
  }

  // We receive the expression as the node
  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    return
    // Adding the assignation operator
    strategy.apply(node, visitor)
        +
        // Formatting the expression
        ((FormatterVisitor) node.accept(visitor)).getCurrentCode();
  }
}
