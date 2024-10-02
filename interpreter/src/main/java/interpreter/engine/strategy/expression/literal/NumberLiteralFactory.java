package interpreter.engine.strategy.expression.literal;

import ast.literal.Literal;
import ast.literal.NumberLiteral;
import position.Position;

public class NumberLiteralFactory implements LiteralFactory {
  @Override
  public Literal<?> createLiteral(String userInput, Position start, Position end) {
    Number number;
    if (userInput.contains(".") || userInput.matches(".*[eE].*")) {
      number = Double.parseDouble(userInput);
    } else {
      number = Integer.parseInt(userInput);
    }
    return new NumberLiteral(number, start, end);
  }
}
