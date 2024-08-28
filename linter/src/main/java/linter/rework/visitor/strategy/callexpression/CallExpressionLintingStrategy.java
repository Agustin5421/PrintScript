package linter.rework.visitor.strategy.callexpression;

import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.statements.CallExpression;
import java.util.List;
import linter.rework.report.FullReport;
import linter.rework.visitor.strategy.LintingStrategy;

public class CallExpressionLintingStrategy implements LintingStrategy {
  private final List<LintingStrategy> argumentStrategies;

  public CallExpressionLintingStrategy(List<LintingStrategy> argumentStrategies) {
    this.argumentStrategies = argumentStrategies;
  }

  @Override
  public FullReport apply(AstNode node, FullReport fullReport) {
    if (!shouldApply(node)) {
      return fullReport;
    }

    CallExpression callExpression = (CallExpression) node;

    List<AstNode> arguments = callExpression.arguments();

    for (AstNode argument : arguments) {
      for (LintingStrategy argumentStrategy : argumentStrategies) {
        fullReport = argumentStrategy.apply(argument, fullReport);
      }
    }

    return fullReport;
  }

  @Override
  public boolean shouldApply(AstNode node) {
    return node.getType().equals(AstNodeType.CALL_EXPRESSION);
  }
}