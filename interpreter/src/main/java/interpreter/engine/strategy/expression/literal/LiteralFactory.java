package interpreter.engine.strategy.expression.literal;

import ast.literal.Literal;
import position.Position;

public interface LiteralFactory {
  Literal<?> createLiteral(String userInput, Position start, Position end);
}
