package ast.literal;

import ast.root.AstNodeType;
import token.Position;
import visitor.NodeVisitor;

public record StringLiteral(String value, Position start, Position end) implements Literal<String> {

  @Override
  public AstNodeType getNodeType() {
    return AstNodeType.STRING_LITERAL;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    return visitor.visitStringLiteral(this);
  }
}
