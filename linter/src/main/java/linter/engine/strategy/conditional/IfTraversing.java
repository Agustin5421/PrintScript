package linter.engine.strategy.conditional;

import ast.expressions.ExpressionNode;
import ast.root.AstNode;
import ast.statements.IfStatement;
import linter.engine.LinterEngine;
import linter.engine.report.FullReport;
import linter.engine.strategy.LintingStrategy;

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
  public LinterEngine apply(AstNode node, LinterEngine engine) {
    IfStatement ifStatement = (IfStatement) node;

    ExpressionNode condition = ifStatement.getCondition();
    engine = engine.lintNode(condition);

    for (var statement : ifStatement.getThenBlockStatement()) {
      engine = engine.lintNode(statement);
    }

    for (var statement : ifStatement.getElseBlockStatement()) {
      engine = engine.lintNode(statement);
    }

    engine = selfStrategy.apply(ifStatement, engine);

    return engine;
  }
}
