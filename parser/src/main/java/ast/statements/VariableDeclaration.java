package ast.statements;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import token.Position;

public record VariableDeclaration(
    Identifier identifier, Expression expression, Position start, Position end)
    implements Statement {
  private static final AstNodeType type = AstNodeType.VARIABLE_DECLARATION;

  public VariableDeclaration(Identifier identifier, Expression expression) {
    this(identifier, expression, identifier.start(), expression.end());
  }

  @Override
  public String toString() {
    return "VariableDeclaration{" + "identifier=" + identifier + ", expression=" + expression + '}';
  }

  @Override
  public AstNodeType getType() {
    return AstNodeType.VARIABLE_DECLARATION;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    visitor = visitor.visitVarDec(this);
    visitor = identifier().accept(visitor);
    visitor = expression().accept(visitor);
    return visitor;
  }
  // el record tiene de por si un toString pero es con [] y no {} por eso lo overridee
}
