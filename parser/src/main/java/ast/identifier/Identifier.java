package ast.identifier;

import ast.expressions.Expression;
import ast.root.AstNodeType;
import token.Position;

public record Identifier(String name, Position start, Position end) implements Expression {

  @Override
  public AstNodeType getType() {
    return AstNodeType.IDENTIFIER;
  }
}
