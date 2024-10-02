package ast.literal;

import ast.root.AstNodeType;
import position.Position;
import visitor.NodeVisitor;

public record BooleanLiteral(Boolean value, Position start, Position end)
    implements Literal<Boolean> {

  @Override
  public AstNodeType getNodeType() {
    return AstNodeType.BOOLEAN_LITERAL;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    return visitor.visitBooleanLiteral(this);
  }
}
