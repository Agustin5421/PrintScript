package ast.statements;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import token.Position;

public record ConstDeclaration(
    Identifier identifier, Expression expression, Position start, Position end)
    implements StatementNode {
  private static final AstNodeType type = AstNodeType.VARIABLE_DECLARATION;

  public ConstDeclaration(Identifier identifier, Expression expression) {
    this(identifier, expression, identifier.start(), expression.end());
  }

  @Override
  public AstNodeType getNodeType() {
    return AstNodeType.VARIABLE_DECLARATION;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    // return visitor.visit(this);
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
