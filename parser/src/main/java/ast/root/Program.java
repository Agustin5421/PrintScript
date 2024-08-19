package ast.root;

import java.util.List;
import token.Position;

public record Program(List<ASTNode> statements, Position start, Position end) {
  public Program(List<ASTNode> statements) {
    this(
        statements,
        statements.isEmpty() ? new Position(0, 0) : statements.get(0).start(),
        statements.isEmpty() ? new Position(0, 0) : statements.get(statements.size() - 1).end());
  }
}
