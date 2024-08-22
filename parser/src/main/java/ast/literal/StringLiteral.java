package ast.literal;

import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import token.Position;

public record StringLiteral(String value, Position start, Position end) implements Literal<String> {

  @Override
  public String toString() {
    return "LiteralString{" + "expression='" + value + '\'' + '}';
  }

  @Override
  public AstNodeType getType() {
    return AstNodeType.STRING_LITERAL;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    return visitor.visitStringLiteral(this);
  }
}
