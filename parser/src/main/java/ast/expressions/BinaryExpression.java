package ast.expressions;

import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import token.Position;
import visitors.ASTVisitor;

public record BinaryExpression(
    Expression left, Expression right, String operator, Position start, Position end)
    implements Expression {
  public BinaryExpression(Expression left, Expression right, String operator) {
    this(left, right, operator, left.start(), right.end());
  }

  @Override
  public AstNodeType getType() {
    return AstNodeType.BINARY_EXPRESSION;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    visitor = visitor.visit(this);
    visitor = left().accept(visitor);
    visitor = right().accept(visitor);
    return visitor;
  }
}
