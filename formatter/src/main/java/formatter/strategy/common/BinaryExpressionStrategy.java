package formatter.strategy.common;

import ast.expressions.BinaryExpression;
import ast.root.AstNode;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitorV1;
import java.util.List;

public class BinaryExpressionStrategy implements FormattingStrategy {
  // The same strategy but the operator differs
  private final OperatorConcatenationStrategy strategy;

  public BinaryExpressionStrategy(String operator) {
    WhiteSpace whiteSpace = new WhiteSpace();
    this.strategy =
        new OperatorConcatenationStrategy(
            List.of(whiteSpace, new OperatorStrategy(operator), whiteSpace));
  }

  @Override
  public String apply(AstNode node, FormatterVisitorV1 visitor) {
    BinaryExpression binaryExpression = (BinaryExpression) node;
    String formattedCode =
        ((FormatterVisitorV1) binaryExpression.left().accept(visitor)).getCurrentCode();
    // Applying the spaces and operator between the expression
    formattedCode += strategy.apply(binaryExpression, visitor);
    // Formatting the right side of the expression
    formattedCode +=
        ((FormatterVisitorV1) binaryExpression.right().accept(visitor)).getCurrentCode();
    return formattedCode;
  }
}
