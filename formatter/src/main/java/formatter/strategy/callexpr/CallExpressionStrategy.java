package formatter.strategy.callexpr;

import ast.root.AstNode;
import ast.statements.CallExpression;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.CallStrategy;

public class CallExpressionStrategy implements FormattingStrategy {
  private final CallStrategy callStrategy;

  public CallExpressionStrategy(CallStrategy callStrategy) {

    this.callStrategy = callStrategy;
  }

  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    CallExpression callExpression = (CallExpression) node;

    String expressionName = callExpression.methodIdentifier().name();
    CallStrategy newCallStrategy =
        callStrategy.newStrategy(expressionName, callExpression.arguments());

    return newCallStrategy.apply(node, engine);
  }
}
