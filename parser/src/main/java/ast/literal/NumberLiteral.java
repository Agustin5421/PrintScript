package ast.literal;

import ast.root.AstNodeType;
import position.Position;
import visitor.NodeVisitor;

public record NumberLiteral(Number value, Position start, Position end) implements Literal<Number> {
  @Override
  public AstNodeType getNodeType() {
    return AstNodeType.NUMBER_LITERAL;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    return visitor.visitNumberLiteral(this);
  }
}
