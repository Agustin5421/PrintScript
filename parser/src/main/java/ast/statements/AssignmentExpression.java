package ast.statements;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.root.AstNodeType;
import token.Position;

public record AssignmentExpression(
    Identifier left, Expression right, String operator, Position start, Position end)
    implements Expression {

  public AssignmentExpression(Identifier left, Expression right, String operator) {
    this(left, right, operator, left.start(), right.end());
  }

  @Override
  public AstNodeType getType() {
    return AstNodeType.ASSIGNMENT_EXPRESSION;
  }
}
