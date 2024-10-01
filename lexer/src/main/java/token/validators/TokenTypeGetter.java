package token.validators;

import java.util.List;
import token.types.TokenSyntaxType;
import token.types.TokenType;

public class TokenTypeGetter implements TypeGetter {

  private final List<TypeGetter> validators;

  public TokenTypeGetter(List<TypeGetter> validators) {
    this.validators = validators;
  }

  public TokenType getType(String word) {
    for (TypeGetter validator : validators) {
      TokenType type = validator.getType(word);
      if (type != null) {
        return type;
      }
    }

    // TODO: throw exception
    return TokenSyntaxType.INVALID;
  }
}
