package ast.literal;

import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import token.Position;

public class BooleanLiteral implements Literal<Boolean> {
  private final boolean value;
  private final Position start;
  private final Position end;

  public BooleanLiteral(boolean value, Position start, Position end) {
    this.value = value;
    this.start = start;
    this.end = end;
  }

  @Override
  public Boolean value() {
    return value;
  }

  @Override
  public AstNodeType getNodeType() {
    return AstNodeType.BOOLEAN_LITERAL;
  }

  @Override
  public Position start() {
    return start;
  }

  @Override
  public Position end() {
    return end;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    return visitor.visitBooleanLiteral(this);
  }
}
