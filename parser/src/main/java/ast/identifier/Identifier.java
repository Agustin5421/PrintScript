package ast.identifier;

import ast.expressions.ExpressionNode;
import ast.root.AstNodeType;
import java.util.Map;
import token.Position;
import token.types.TokenDataType;
import visitor.NodeVisitor;

public record Identifier(
    String name, Position start, Position end, TokenDataType type, Map<String, Boolean> modifiers)
    implements ExpressionNode {

  // This constructor is used by the parser to create an Identifier node v1
  public Identifier(String name, Position start, Position end) {
    this(name, start, end, TokenDataType.STRING_TYPE, Map.of());
  }

  @Override
  public AstNodeType getNodeType() {
    return AstNodeType.IDENTIFIER;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    return visitor.visitIdentifier(this);
  }
}
