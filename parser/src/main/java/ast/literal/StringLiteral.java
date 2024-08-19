package ast.literal;

import ast.root.AstNodeType;
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
}
