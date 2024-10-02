package interpreter.engine.strategy.expression.literal;

import ast.literal.BooleanLiteral;
import ast.literal.Literal;
import position.Position;

public class BooleanLiteralFactory implements LiteralFactory {
  @Override
  public Literal<?> createLiteral(String userInput, Position start, Position end) {
    boolean bool = userInput.equalsIgnoreCase("true") || userInput.equalsIgnoreCase("t");
    return new BooleanLiteral(bool, start, end);
  }
}
