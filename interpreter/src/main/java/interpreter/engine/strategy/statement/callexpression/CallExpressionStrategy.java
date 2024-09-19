package interpreter.engine.strategy.statement.callexpression;

import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.statements.CallExpression;
import interpreter.engine.InterpreterEngine;
import interpreter.engine.strategy.statement.StatementStrategy;
import strategy.StrategyContainer;

public class CallExpressionStrategy implements StatementStrategy {
  private final StrategyContainer<String, StatementStrategy> statementsContainer;

  public CallExpressionStrategy(StrategyContainer<String, StatementStrategy> statementsContainer) {
    this.statementsContainer = statementsContainer;
  }

  @Override
  public InterpreterEngine apply(AstNode node, InterpreterEngine engine) {
    CallExpression methodCall = (CallExpression) node;
    Identifier methodName = methodCall.methodIdentifier();

    StatementStrategy methodStrategy = statementsContainer.getStrategy(methodName.name());

    return methodStrategy.apply(node, engine);
  }
}
