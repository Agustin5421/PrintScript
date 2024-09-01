package token.validators.literal;

import token.types.TokenType;
import token.types.TokenValueType;

public class StringTypePatternChecker implements LiteralPatternChecker {
  @Override
  public TokenType getType(String word) {
    if (isString(word)) {
      return TokenValueType.STRING;
    }
    return null;
  }

  private static boolean isString(String value) {
    return (value.startsWith("\"") && value.endsWith("\""))
        || (value.startsWith("'") && value.endsWith("'"));
  }
}
