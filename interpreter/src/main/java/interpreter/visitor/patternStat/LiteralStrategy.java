package interpreter.visitor.patternStat;

import ast.literal.Literal;
import token.Position;

public interface LiteralStrategy {
  Literal<?> createLiteral(String userInput, Position start, Position end);
}
