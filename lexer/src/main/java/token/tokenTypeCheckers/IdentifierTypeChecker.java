package token.tokenTypeCheckers;

import java.util.regex.Pattern;
import token.tokenTypes.TokenTagType;
import token.tokenTypes.TokenType;

public class IdentifierTypeChecker implements TypeGetter {

  private static final Pattern PRINTSCRIPT_IDENTIFIER_PATTERN =
      Pattern.compile("^[a-zA-Z_][a-zA-Z\\d_]*$");

  @Override
  public TokenType getType(String word) {
    if (isValidIdentifier(word)) {
      return TokenTagType.IDENTIFIER;
    }
    return TokenTagType.INVALID;
  }

  private boolean isValidIdentifier(String word) {
    return word != null
        && !word.isEmpty()
        && PRINTSCRIPT_IDENTIFIER_PATTERN.matcher(word).matches();
  }
}
