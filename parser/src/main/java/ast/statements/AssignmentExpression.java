package ast.statements;

import ast.expressions.ExpressionNode;
import ast.identifier.Identifier;
import ast.root.AstNodeType;
import token.Position;
import visitor.NodeVisitor;

public record AssignmentExpression(
    Identifier left, ExpressionNode right, String operator, Position start, Position end)
    implements StatementNode {

  public AssignmentExpression(Identifier left, ExpressionNode right, String operator) {
    this(left, right, operator, left.start(), right.end());
  }

  @Override
  public AstNodeType getNodeType() {
    return AstNodeType.ASSIGNMENT_EXPRESSION;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    return visitor.visitAssignmentExpression(this);
  }
}
