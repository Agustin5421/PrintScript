package token.validators;

import java.util.Map;
import token.types.TokenType;

public class SyntaxPatternChecker implements TypeGetter {

  private final Map<String, TokenType> reservedWords;

  public SyntaxPatternChecker(Map<String, TokenType> reservedWords) {
    this.reservedWords = reservedWords;
  }

  @Override
  public TokenType getType(String word) {
    return reservedWords.getOrDefault(word, null);
  }
}
