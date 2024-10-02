package linter.engine.strategy.assign;

import ast.expressions.ExpressionNode;
import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import linter.engine.LinterEngine;
import linter.engine.strategy.LintingStrategy;
import report.FullReport;

public class AssignmentExpressionTraversing implements LintingStrategy {
  private final LintingStrategy selfStrategy;

  public AssignmentExpressionTraversing(LintingStrategy selfStrategy) {
    this.selfStrategy = selfStrategy;
  }

  @Override
  public FullReport oldApply(AstNode node, FullReport fullReport) {
    return null;
  }

  @Override
  public LinterEngine apply(AstNode node, LinterEngine engine) {
    AssignmentExpression assignmentExpression = (AssignmentExpression) node;

    Identifier left = assignmentExpression.left();
    engine = engine.lintNode(left);

    ExpressionNode right = assignmentExpression.right();
    engine = engine.lintNode(right);

    engine = selfStrategy.apply(assignmentExpression, engine);

    return engine;
  }
}
