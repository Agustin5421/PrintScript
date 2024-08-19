package token.tokenTypeCheckers;

import token.tokenTypes.TokenType;

public interface TypeGetter {
  TokenType getType(String word);
}
