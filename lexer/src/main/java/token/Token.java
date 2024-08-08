package token;

import token.tokenTypes.TokenType;

public class Token {
    private final TokenType type;
    private final String value;
    private final int line;
    private final int column;

    public Token(TokenType type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getRow() {
        return line;
    }

    public int getCol() {
        return column;
    }

    @Override
    public String toString() {
        return String.format("Token(type: %s, value: %s, row: %d, col: %d)", type, value, line, column);
    }
}
