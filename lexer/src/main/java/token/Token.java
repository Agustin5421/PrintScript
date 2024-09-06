package token;

import token.types.TokenType;

public record Token(
        TokenType nodeType, String value, Position initialPosition, Position finalPosition) {

  @Override
  public String toString() {
    return String.format(
        "Token(nodeType: %s, value: %s, initialP: %s, finalP: %s)",
            nodeType, value, initialPosition, finalPosition);
  }
}
