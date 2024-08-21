package token.validators;

import token.types.TokenType;

public interface TypeGetter {
  TokenType getType(String word);
}
