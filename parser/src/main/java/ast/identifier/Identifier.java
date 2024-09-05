package ast.identifier;

import ast.expressions.Expression;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import token.Position;
import token.types.TokenDataType;

public record Identifier(String name, Position start, Position end, TokenDataType type)
    implements Expression {

  public Identifier(String name, Position start, Position end) {
    this(name, start, end, TokenDataType.STRING_TYPE);
  }

  @Override
  public AstNodeType getNodeType() {
    return AstNodeType.IDENTIFIER;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    return visitor.visitIdentifier(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Identifier that = (Identifier) obj;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
