package token.validators.literal;

import static org.codehaus.groovy.runtime.StringGroovyMethods.isNumber;

import token.types.TokenType;
import token.types.TokenValueType;

public class NumberTypePatternChecker implements LiteralPatternChecker {
  @Override
  public TokenType getType(String word) {
    if (isNumber(word)) {
      return TokenValueType.NUMBER;
    }
    return null;
  }
}
