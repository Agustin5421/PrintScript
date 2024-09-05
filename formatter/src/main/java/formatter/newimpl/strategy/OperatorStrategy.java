package formatter.newimpl.strategy;

import ast.root.AstNode;
import formatter.newimpl.FormatterVisitor2;

public class OperatorStrategy implements FormattingStrategy {
  // Single operator that can be :, =, +=, +, -, /, *...
  private final String operator;

  public OperatorStrategy(String operator) {
    this.operator = operator;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor2 visitor) {
    return operator;
  }
}
