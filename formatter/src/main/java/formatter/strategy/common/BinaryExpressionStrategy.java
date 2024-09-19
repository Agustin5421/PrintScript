package formatter.strategy.common;

import ast.expressions.BinaryExpression;
import ast.root.AstNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;

public class BinaryExpressionStrategy implements FormattingStrategy {
  // The same strategy but the operator differs
  private final CharacterStrategy beforeSpace;
  private final CharacterStrategy afterSpace;

  public BinaryExpressionStrategy(CharacterStrategy beforeSpace, CharacterStrategy afterSpace) {
    this.beforeSpace = beforeSpace;
    this.afterSpace = afterSpace;
  }

  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    BinaryExpression binaryExpression = (BinaryExpression) node;

    // Writing the left side of the expression
    engine.format(binaryExpression.left());

    // Applying the spaces and operator between the expression
    beforeSpace.apply(binaryExpression, engine);
    engine.write(binaryExpression.operator());
    afterSpace.apply(binaryExpression, engine);

    // Writing the right side of the expression
    return engine.format(binaryExpression.right());
  }
}
