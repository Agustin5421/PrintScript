package interpreter.visitor.strategy.literal;

import ast.literal.Literal;
import ast.root.AstNode;
import ast.visitor.NodeVisitor;
import interpreter.ValueCollector;
import interpreter.visitor.strategy.InterpretingStrategy;

public class LiteralStrategy implements InterpretingStrategy {
  @Override
  public NodeVisitor interpret(AstNode node, NodeVisitor visitor) {
    ValueCollector valueCollector = (ValueCollector) visitor;
    Literal<?> literalNode = (Literal<?>) node;
    return valueCollector.setValue(literalNode);
  }
}
