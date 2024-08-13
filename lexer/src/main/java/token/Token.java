package token;

import token.tokenTypes.TokenType;

public class Token {
    private final TokenType type;
    private final String value;
    private final TokenPosition initialPosition;
    private final TokenPosition finalPosition;

    public Token(TokenType type, String value, TokenPosition initialPosition, TokenPosition finalPosition) {
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

    public TokenPosition getInitialPosition() {
        return initialPosition;
    }

    public TokenPosition getFinalPosition() {
        return finalPosition;
    }

    @Override
    public String toString() {
        return String.format("Token(type: %s, value: %s, initialP: %s, finalP: %s)", type, value, initialPosition, finalPosition);
    }
}
