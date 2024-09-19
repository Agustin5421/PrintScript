package linter.visitor.strategy.assign;

import ast.expressions.ExpressionNode;
import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import linter.visitor.report.FullReport;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.NewLinterVisitor;

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
  public NewLinterVisitor apply(AstNode node, NewLinterVisitor visitor) {
    AssignmentExpression assignmentExpression = (AssignmentExpression) node;

    Identifier left = assignmentExpression.left();
    visitor = visitor.lintNode(left);

    ExpressionNode right = assignmentExpression.right();
    visitor = visitor.lintNode(right);

    visitor = selfStrategy.apply(assignmentExpression, visitor);

    return visitor;
  }
}
