package linter.engine.strategy.binary;

import ast.expressions.BinaryExpression;
import ast.expressions.ExpressionNode;
import ast.root.AstNode;
import linter.engine.LinterEngine;
import linter.engine.strategy.LintingStrategy;
import report.FullReport;

public class BinaryExpressionTraversing implements LintingStrategy {
  private final LintingStrategy selfStrategy;

  public BinaryExpressionTraversing(LintingStrategy selfStrategy) {
    this.selfStrategy = selfStrategy;
  }

  @Override
  public FullReport oldApply(AstNode node, FullReport fullReport) {
    return null;
  }

  @Override
  public LinterEngine apply(AstNode node, LinterEngine engine) {
    BinaryExpression binaryExpression = (BinaryExpression) node;

    ExpressionNode left = binaryExpression.left();
    engine = engine.lintNode(left);

    ExpressionNode right = binaryExpression.right();
    engine = engine.lintNode(right);

    engine = selfStrategy.apply(binaryExpression, engine);

    return engine;
  }
}
