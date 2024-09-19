package linter.visitor.strategy.binary;

import ast.expressions.BinaryExpression;
import ast.expressions.ExpressionNode;
import ast.root.AstNode;
import linter.visitor.report.FullReport;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.NewLinterVisitor;

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
  public NewLinterVisitor apply(AstNode node, NewLinterVisitor visitor) {
    BinaryExpression binaryExpression = (BinaryExpression) node;

    ExpressionNode left = binaryExpression.left();
    visitor = visitor.lintNode(left);

    ExpressionNode right = binaryExpression.right();
    visitor = visitor.lintNode(right);

    visitor = selfStrategy.apply(binaryExpression, visitor);

    return visitor;
  }
}
