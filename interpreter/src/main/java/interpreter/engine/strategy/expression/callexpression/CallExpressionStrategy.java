package interpreter.engine.strategy.expression.callexpression;

import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.statements.CallExpression;
import interpreter.engine.ValueCollector;
import interpreter.engine.strategy.expression.ExpressionStrategy;
import strategy.StrategyContainer;

public class CallExpressionStrategy implements ExpressionStrategy {
  private final StrategyContainer<String, ExpressionStrategy> expressionsContainer;

  public CallExpressionStrategy(
      StrategyContainer<String, ExpressionStrategy> expressionsContainer) {
    this.expressionsContainer = expressionsContainer;
  }

  @Override
  public ValueCollector apply(AstNode node, ValueCollector engine) {
    CallExpression methodCall = (CallExpression) node;
    Identifier methodName = methodCall.methodIdentifier();

    ExpressionStrategy methodStrategy = expressionsContainer.getStrategy(methodName.name());

    return methodStrategy.apply(node, engine);
  }
}
