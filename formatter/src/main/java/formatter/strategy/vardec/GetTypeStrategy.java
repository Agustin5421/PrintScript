package formatter.strategy.vardec;

import ast.expressions.BinaryExpression;
import ast.expressions.ExpressionNode;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.statements.VariableDeclaration;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitorV1;

public class GetTypeStrategy implements FormattingStrategy {
  @Override
  public String apply(AstNode node, FormatterVisitorV1 visitor) {
    VariableDeclaration variableDeclaration = (VariableDeclaration) node;
    ExpressionNode expression = variableDeclaration.expression();
    return getType(expression);
  }

  // Returning the nodeType equivalent of printscript (only working for string and numbers for now)
  private String getType(ExpressionNode expression) throws IllegalArgumentException {
    if (containsStringLiteral(expression)) {
      return "string";
    }
    return "number";
  }

  private boolean containsStringLiteral(ExpressionNode expression) {
    if (expression instanceof StringLiteral) {
      return true;
    } else if (expression instanceof BinaryExpression binaryExpression) {
      return containsStringLiteral(binaryExpression.left())
          || containsStringLiteral(binaryExpression.right());
    }
    return false;
  }
}
