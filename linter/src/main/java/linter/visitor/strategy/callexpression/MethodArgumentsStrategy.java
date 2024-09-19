package linter.visitor.strategy.callexpression;

import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.statements.CallExpression;
import linter.visitor.LinterVisitor;
import linter.visitor.report.FullReport;
import linter.visitor.strategy.LintingStrategy;

public class MethodArgumentsStrategy implements LintingStrategy {
  private final String methodName;
  private final LintingStrategy argumentsStrategy;

  public MethodArgumentsStrategy(String methodName, LintingStrategy argumentsStrategy) {
    this.methodName = methodName;
    this.argumentsStrategy = argumentsStrategy;
  }

  @Override
  public FullReport apply(AstNode node, FullReport fullReport) {
    if (!shouldApply(node)) {
      return fullReport;
    }

    return argumentsStrategy.apply(node, fullReport);
  }

  @Override
  public LinterVisitor apply(AstNode node, LinterVisitor visitor) {
    if (!shouldApply(node)) {
      return visitor;
    }

    return argumentsStrategy.apply(node, visitor);
  }

  private boolean shouldApply(AstNode node) {
    boolean correctType = node.getNodeType().equals(AstNodeType.CALL_EXPRESSION);

    if (!correctType) {
      return false;
    }

    try {
      CallExpression callExpression = (CallExpression) node;
      return callExpression.methodIdentifier().name().equals(methodName);
    } catch (Exception e) {
      return false;
    }
  }
}
