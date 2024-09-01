package token.validators;

import java.util.regex.Pattern;
import token.types.TokenSyntaxType;
import token.types.TokenType;

public class IdentifierTypeChecker implements TypeGetter {

  private final Pattern identifierPattern;

  public IdentifierTypeChecker(Pattern identifierPattern) {
    this.identifierPattern = identifierPattern;
  }

  @Override
  public TokenType getType(String word) {
    if (isValidIdentifier(word)) {
      return TokenSyntaxType.IDENTIFIER;
    }
    return null;
  }

  private boolean isValidIdentifier(String word) {
    return word != null && !word.isEmpty() && identifierPattern.matcher(word).matches();
  }
}
