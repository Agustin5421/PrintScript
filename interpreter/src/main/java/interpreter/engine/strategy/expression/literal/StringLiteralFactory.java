package interpreter.engine.strategy.expression.literal;

import ast.literal.Literal;
import ast.literal.StringLiteral;
import token.Position;

public class StringLiteralFactory implements LiteralFactory {
  @Override
  public Literal<?> createLiteral(String userInput, Position start, Position end) {
    return new StringLiteral(userInput, start, end);
  }
}
