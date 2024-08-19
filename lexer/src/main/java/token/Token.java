package token;

import token.types.TokenType;

public class Token {
  private final TokenType type;
  private final String value;
  private final Position initialPosition;
  private final Position finalPosition;

  public Token(TokenType type, String value, Position initialPosition, Position finalPosition) {
    this.type = type;
    this.value = value;
    this.initialPosition = initialPosition;
    this.finalPosition = finalPosition;
  }

  public TokenType getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  public Position getInitialPosition() {
    return initialPosition;
  }

  public Position getFinalPosition() {
    return finalPosition;
  }

  @Override
  public String toString() {
    return String.format(
        "Token(type: %s, value: %s, initialP: %s, finalP: %s)",
        type, value, initialPosition, finalPosition);
  }
}
