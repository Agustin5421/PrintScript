package token.validators.literal;

import token.types.TokenType;
import token.types.TokenValueType;

public class BooleanTypePatternChecker implements LiteralPatternChecker {
  @Override
  public TokenType getType(String word) {
    if (isBoolean(word)) {
      return TokenValueType.BOOLEAN;
    }
    return null;
  }

  private static boolean isBoolean(String value) {
    return value.equals("true") || value.equals("false");
  }
}
