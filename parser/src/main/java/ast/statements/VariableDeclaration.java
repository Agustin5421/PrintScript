package ast.statements;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import token.Position;

public record VariableDeclaration(
    String kind,
    Identifier identifier,
    Expression expression,
    String varType,
    Position start,
    Position end)
    implements StatementNode {
  private static final AstNodeType type = AstNodeType.VARIABLE_DECLARATION;

  // TODO remove this constructor
  public VariableDeclaration(Identifier identifier, Expression expression) {
    this("test", identifier, expression, "test", identifier.start(), expression.end());
  }

  @Override
  public AstNodeType getNodeType() {
    return AstNodeType.VARIABLE_DECLARATION;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    return visitor.visitVarDec(this);
  }
}
