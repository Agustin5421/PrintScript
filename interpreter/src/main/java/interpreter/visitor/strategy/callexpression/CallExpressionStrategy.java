package interpreter.visitor.strategy.callexpression;

import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.statements.CallExpression;
import ast.visitor.NodeVisitor;
import interpreter.visitor.strategy.InterpretingStrategy;
import interpreter.visitor.strategy.StrategyContainer;

public class CallExpressionStrategy implements InterpretingStrategy {
  private final StrategyContainer<String> methodsContainer;

  public CallExpressionStrategy(StrategyContainer<String> methodsContainer) {
    this.methodsContainer = methodsContainer;
  }

  @Override
  public NodeVisitor interpret(AstNode node, NodeVisitor visitor) {
    CallExpression methodCall = (CallExpression) node;
    Identifier methodName = methodCall.methodIdentifier();

    InterpretingStrategy methodStrategy = methodsContainer.getStrategy(methodName.name());

    return methodStrategy.interpret(node, visitor);
  }
}
