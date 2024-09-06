package ast.statements;

import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import java.util.List;
import token.Position;

public record CallExpression(
    Identifier methodIdentifier, List<AstNode> arguments, Position start, Position end)
    implements StatementNode {

  public CallExpression(Identifier methodIdentifier, List<AstNode> arguments) {
    this(
        methodIdentifier,
        arguments,
        methodIdentifier.start(),
        arguments.isEmpty() ? methodIdentifier.end() : arguments.get(arguments.size() - 1).end());
  }

  @Override
  public AstNodeType getNodeType() {
    return AstNodeType.CALL_EXPRESSION;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    return visitor.visitCallExpression(this);
  }
}
