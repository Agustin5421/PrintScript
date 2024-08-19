package token.validators;

import java.util.List;
import token.types.TokenTagType;
import token.types.TokenType;

public class TokenTypeChecker implements TypeGetter {

  private final List<TypeGetter> validators;

  public TokenTypeChecker(List<TypeGetter> validators) {
    this.validators = validators;
  }

  public TokenType getType(String word) {
    for (TypeGetter validator : validators) {
      TokenType type = validator.getType(word);
      if (type != null) {
        return type;
      }
    }
    return TokenTagType.INVALID;
  }
}
