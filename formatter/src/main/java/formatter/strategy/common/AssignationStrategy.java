package formatter.strategy.common;

import ast.root.AstNode;
import ast.root.AstNodeType;
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
        getExpressionCode(node, visitor);
  }

  public String getExpressionCode(AstNode node, FormatterVisitor visitor) {
    String expressionCode = ((FormatterVisitor) node.accept(visitor)).getCurrentCode();
    if (node.getNodeType().equals(AstNodeType.CALL_EXPRESSION)) {
      // If we get a call expression, we need to remove the semicolon
      // and line break
      expressionCode = expressionCode.substring(0, expressionCode.length() - 2);
    }
    return expressionCode;
  }
}
