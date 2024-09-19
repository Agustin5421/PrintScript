package linter.visitor.strategy.conditional;

import ast.expressions.ExpressionNode;
import ast.root.AstNode;
import ast.statements.IfStatement;
import linter.visitor.report.FullReport;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.NewLinterVisitor;

public class IfTraversing implements LintingStrategy {
  private final LintingStrategy selfStrategy;

  public IfTraversing(LintingStrategy selfStrategy) {
    this.selfStrategy = selfStrategy;
  }

  @Override
  public FullReport oldApply(AstNode node, FullReport fullReport) {
    return null;
  }

  @Override
  public NewLinterVisitor apply(AstNode node, NewLinterVisitor visitor) {
    IfStatement ifStatement = (IfStatement) node;

    ExpressionNode condition = ifStatement.getCondition();
    visitor = visitor.lintNode(condition);

    for (var statement : ifStatement.getThenBlockStatement()) {
      visitor = visitor.lintNode(statement);
    }

    for (var statement : ifStatement.getElseBlockStatement()) {
      visitor = visitor.lintNode(statement);
    }

    visitor = selfStrategy.apply(ifStatement, visitor);

    return visitor;
  }
}
