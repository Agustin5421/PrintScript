package ast.literal;

import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import token.Position;

public record StringLiteral(String value, Position start, Position end) implements Literal<String> {

  @Override
  public AstNodeType getNodeType() {
    return AstNodeType.STRING_LITERAL;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    return visitor.visit(this);
  }
}
