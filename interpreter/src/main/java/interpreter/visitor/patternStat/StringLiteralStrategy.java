package interpreter.visitor.patternStat;

import ast.literal.Literal;
import ast.literal.StringLiteral;
import token.Position;

public class StringLiteralStrategy implements LiteralStrategy {
  @Override
  public Literal<?> createLiteral(String userInput, Position start, Position end) {
    return new StringLiteral(userInput, start, end);
  }
}
