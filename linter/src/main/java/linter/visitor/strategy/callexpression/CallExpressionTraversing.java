package linter.visitor.strategy.callexpression;

import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.statements.CallExpression;
import linter.visitor.report.FullReport;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.NewLinterVisitor;

public class CallExpressionTraversing implements LintingStrategy {
  private final LintingStrategy selfStrategy;

  public CallExpressionTraversing(LintingStrategy selfStrategy) {
    this.selfStrategy = selfStrategy;
  }

  @Override
  public FullReport oldApply(AstNode node, FullReport fullReport) {
    return null;
  }

  @Override
  public NewLinterVisitor apply(AstNode node, NewLinterVisitor visitor) {
    CallExpression callExpression = (CallExpression) node;

    Identifier methodIdentifier = callExpression.methodIdentifier();
    visitor = visitor.lintNode(methodIdentifier);

    for (AstNode argument : callExpression.arguments()) {
      visitor = visitor.lintNode(argument);
    }

    visitor = selfStrategy.apply(callExpression, visitor);

    return visitor;
  }
}
