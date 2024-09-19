package interpreter.engine.strategy.expression.literal;

import ast.literal.Literal;
import ast.root.AstNode;
import interpreter.engine.ValueCollector;
import interpreter.engine.strategy.expression.ExpressionStrategy;

public class LiteralStrategy implements ExpressionStrategy {
  @Override
  public ValueCollector apply(AstNode node, ValueCollector engine) {
    Literal<?> literalNode = (Literal<?>) node;
    return engine.setValue(literalNode);
  }
}
