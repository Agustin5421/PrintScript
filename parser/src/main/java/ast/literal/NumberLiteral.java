package ast.literal;

import ast.root.AstNodeType;
import token.Position;

public record NumberLiteral(Number value, Position start, Position end) implements Literal<Number> {

  @Override
  public String toString() {
    return "LiteralNumber{" + "expression=" + value + '}';
  }

  @Override
  public AstNodeType getType() {
    return AstNodeType.NUMBER_LITERAL;
  }
}
