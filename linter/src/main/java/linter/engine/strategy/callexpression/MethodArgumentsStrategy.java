package linter.engine.strategy.callexpression;

import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.statements.CallExpression;
import linter.engine.LinterEngine;
import linter.engine.strategy.LintingStrategy;

public class MethodArgumentsStrategy implements LintingStrategy {
  private final String methodName;
  private final LintingStrategy argumentsStrategy;

  public MethodArgumentsStrategy(String methodName, LintingStrategy argumentsStrategy) {
    this.methodName = methodName;
    this.argumentsStrategy = argumentsStrategy;
  }

  @Override
  public LinterEngine apply(AstNode node, LinterEngine engine) {
    if (!shouldApply(node)) {
      return engine;
    }

    return argumentsStrategy.apply(node, engine);
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
