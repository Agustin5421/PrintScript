package ast.statements;

import ast.expressions.ExpressionNode;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import java.util.List;
import token.Position;

public class IfStatement implements StatementNode {
  private final Position start;
  private final Position end;
  private final ExpressionNode condition;
  private final List<StatementNode> thenBlockStatement;
  private final List<StatementNode> elseBlockStatement;

  public IfStatement(
      Position start,
      Position end,
      ExpressionNode condition,
      List<StatementNode> thenStatement,
      List<StatementNode> elseStatement) {
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
    return visitor.visit(this);
  }

  public ExpressionNode getCondition() {
    return condition;
  }

  public List<StatementNode> getThenBlockStatement() {
    return thenBlockStatement;
  }

  public List<StatementNode> getElseBlockStatement() {
    return elseBlockStatement;
  }
}
