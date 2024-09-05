package formatter.newimpl.strategy;

import ast.expressions.BinaryExpression;
import ast.expressions.Expression;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.statements.VariableDeclaration;
import formatter.newimpl.FormatterVisitor2;

public class GetTypeStrategy implements FormattingStrategy {
  @Override
  public String apply(AstNode node, FormatterVisitor2 visitor) {
    VariableDeclaration variableDeclaration = (VariableDeclaration) node;
    Expression expression = variableDeclaration.expression();
    return getType(expression);
  }

  // Returning the type equivalent of printscript (only working for string and numbers for now)
  private String getType(Expression expression) throws IllegalArgumentException {
    if (containsStringLiteral(expression)) {
      return "string";
    }
    return "number";
  }

  private boolean containsStringLiteral(Expression expression) {
    if (expression instanceof StringLiteral) {
      return true;
    } else if (expression instanceof BinaryExpression binaryExpression) {
      return containsStringLiteral(binaryExpression.left())
          || containsStringLiteral(binaryExpression.right());
    }
    return false;
  }
}
