package token;

import token.types.TokenType;

public record Token(
    TokenType type, String value, Position initialPosition, Position finalPosition) {

  @Override
  public String toString() {
    return String.format(
        "Token(type: %s, value: %s, initialP: %s, finalP: %s)",
        type, value, initialPosition, finalPosition);
  }
}
