package token;

import token.types.TokenType;

public record Token(
    TokenType nodeType, String value, Position initialPosition, Position finalPosition) {

  @Override
  public String toString() {
    return value;
  }
}
