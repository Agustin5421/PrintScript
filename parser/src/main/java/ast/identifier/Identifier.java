package ast.identifier;

import ast.expressions.Expression;
import ast.root.ASTNodeType;
import token.Position;

public record Identifier(String name, Position start, Position end) implements Expression {

  @Override
  public ASTNodeType getType() {
    return ASTNodeType.IDENTIFIER;
  }
}
