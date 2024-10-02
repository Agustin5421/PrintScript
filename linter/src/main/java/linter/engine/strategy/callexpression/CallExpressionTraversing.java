package linter.engine.strategy.callexpression;

import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.statements.CallExpression;
import linter.engine.LinterEngine;
import linter.engine.strategy.LintingStrategy;

public class CallExpressionTraversing implements LintingStrategy {
  private final LintingStrategy selfStrategy;

  public CallExpressionTraversing(LintingStrategy selfStrategy) {
    this.selfStrategy = selfStrategy;
  }

  @Override
  public LinterEngine apply(AstNode node, LinterEngine engine) {
    CallExpression callExpression = (CallExpression) node;

    Identifier methodIdentifier = callExpression.methodIdentifier();
    engine = engine.lintNode(methodIdentifier);

    for (AstNode argument : callExpression.arguments()) {
      engine = engine.lintNode(argument);
    }

    engine = selfStrategy.apply(callExpression, engine);

    return engine;
  }
}
