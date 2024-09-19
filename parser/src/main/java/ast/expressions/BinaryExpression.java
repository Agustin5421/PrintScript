package ast.expressions;

import ast.root.AstNodeType;
import token.Position;
import visitor.NodeVisitor;

public record BinaryExpression(
    ExpressionNode left, ExpressionNode right, String operator, Position start, Position end)
    implements ExpressionNode {
  public BinaryExpression(ExpressionNode left, ExpressionNode right, String operator) {
    this(left, right, operator, left.start(), right.end());
  }

  @Override
  public AstNodeType getNodeType() {
    return AstNodeType.BINARY_EXPRESSION;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    return visitor.visitBinaryExpression(this);
  }
}
