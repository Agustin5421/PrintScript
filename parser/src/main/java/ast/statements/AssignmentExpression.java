package ast.statements;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import token.Position;
import visitors.ASTVisitor;

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

    @Override
    public ASTVisitor accept(ASTVisitor visitor) {
        visitor = visitor.visitAssignmentExpression(this);
        visitor = left().accept(visitor);
        visitor = right().accept(visitor);

        return visitor;
    }
  @Override
  public AstNodeType getType() {
    return AstNodeType.ASSIGNMENT_EXPRESSION;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    visitor = visitor.visit(this);
    visitor = left().accept(visitor);
    visitor = right().accept(visitor);
    return visitor;
  }
}
