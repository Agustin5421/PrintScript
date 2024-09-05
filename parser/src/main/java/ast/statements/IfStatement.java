package ast.statements;

import ast.expressions.Expression;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import java.util.List;
import token.Position;

public class IfStatement implements Statement {
  private final Position start;
  private final Position end;
  private final Expression condition;
  private final List<Statement> thenBlockStatement;
  private final List<Statement> elseBlockStatement;

  public IfStatement(
      Position start,
      Position end,
      Expression condition,
      List<Statement> thenStatement,
      List<Statement> elseStatement) {
    this.start = start;
    this.end = end;
    this.condition = condition;
    this.thenBlockStatement = thenStatement;
    this.elseBlockStatement = elseStatement;
  }

  @Override
  public AstNodeType getNodeType() {
    return AstNodeType.IF_STATEMENT;
  }

  @Override
  public Position start() {
    return start;
  }

  @Override
  public Position end() {
    return end;
  }

  @Override
  public NodeVisitor accept(NodeVisitor visitor) {
    return visitor.visitIfStatement(this);
  }

  public Expression getCondition() {
    return condition;
  }

  public List<Statement> getThenBlockStatement() {
    return thenBlockStatement;
  }

  public List<Statement> getElseBlockStatement() {
    return elseBlockStatement;
  }
}
