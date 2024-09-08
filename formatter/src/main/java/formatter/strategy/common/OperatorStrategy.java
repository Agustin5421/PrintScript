package formatter.strategy.common;

import ast.root.AstNode;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitor;

public class OperatorStrategy implements FormattingStrategy {
  // Single operator that can be :, =, +=, +, -, /, *...
  private final String operator;

  public OperatorStrategy(String operator) {
    this.operator = operator;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    return operator;
  }
}
