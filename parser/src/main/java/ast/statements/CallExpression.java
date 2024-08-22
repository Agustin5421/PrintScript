package ast.statements;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import java.util.List;
import token.Position;

public record CallExpression(
    Identifier methodIdentifier,
    List<AstNode> arguments,
    boolean optionalParameters,
    Position start,
    Position end)
    implements Expression {

  public CallExpression(
      Identifier methodIdentifier, List<AstNode> arguments, boolean optionalParameters) {
    this(
        methodIdentifier,
        arguments,
        optionalParameters,
        methodIdentifier.start(),
        arguments.isEmpty() ? methodIdentifier.end() : arguments.get(arguments.size() - 1).end());
  }

  @Override
  public AstNodeType getType() {
    return AstNodeType.CALL_EXPRESSION;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    visitor = visitor.visit(this);
    visitor = methodIdentifier().accept(visitor);
    for (AstNode argument : arguments()) {
      visitor = argument.accept(visitor);
    }
    return visitor;
  }
}
