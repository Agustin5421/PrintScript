package interpreter.visitor.patternStat;

import ast.literal.BooleanLiteral;
import ast.literal.Literal;
import token.Position;

public class BooleanLiteralStrategy implements LiteralStrategy {
  @Override
  public Literal<?> createLiteral(String userInput, Position start, Position end) {
    boolean bool = userInput.equalsIgnoreCase("true") || userInput.equalsIgnoreCase("t");
    return new BooleanLiteral(bool, start, end);
  }
}
