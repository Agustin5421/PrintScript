package ast.statements;

import ast.expressions.ExpressionNode;
import ast.identifier.Identifier;
import ast.root.AstNodeType;
import token.Position;
import visitor.NodeVisitor;

public record VariableDeclaration(
    String kind,
    Identifier identifier,
    ExpressionNode expression,
    String varType,
    Position start,
    Position end)
    implements StatementNode {
  private static final AstNodeType type = AstNodeType.VARIABLE_DECLARATION;

  public VariableDeclaration(Identifier identifier, ExpressionNode expression) {
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
