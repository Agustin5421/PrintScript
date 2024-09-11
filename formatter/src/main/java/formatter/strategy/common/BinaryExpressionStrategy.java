package formatter.strategy.common;

import ast.expressions.BinaryExpression;
import ast.root.AstNode;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.space.WhiteSpace;
import formatter.visitor.FormatterVisitor;
import java.util.List;

public class BinaryExpressionStrategy implements FormattingStrategy {
  // The same strategy but the operator differs
  private final OperatorConcatenationStrategy strategy;

  public BinaryExpressionStrategy(String operator) {
    WhiteSpace whiteSpace = new WhiteSpace();
    this.strategy =
        new OperatorConcatenationStrategy(
            List.of(whiteSpace, new CharacterStrategy(operator), whiteSpace));
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    BinaryExpression binaryExpression = (BinaryExpression) node;
    String formattedCode =
        ((FormatterVisitor) binaryExpression.left().accept(visitor)).getCurrentCode();
    // Applying the spaces and operator between the expression
    formattedCode += strategy.apply(binaryExpression, visitor);
    // Formatting the right side of the expression
    formattedCode += ((FormatterVisitor) binaryExpression.right().accept(visitor)).getCurrentCode();
    return formattedCode;
  }
}
