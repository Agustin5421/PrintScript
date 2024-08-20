package ast.identifier;

import ast.expressions.Expression;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import token.Position;

public record Identifier(String name, Position start, Position end) implements Expression {

  @Override
  public AstNodeType getType() {
    return AstNodeType.IDENTIFIER;
  }

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visit(this);
  }
}
