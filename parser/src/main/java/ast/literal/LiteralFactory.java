package ast.literal;

import token.Token;
import token.tokenTypes.TokenValueType;

public class LiteralFactory {
    public static Literal<?> createLiteral(Token token) {
        if (token.getType() == TokenValueType.STRING) {
            return new StringLiteral(token.getValue(), token.getInitialPosition(), token.getFinalPosition());
        }

        if (token.getType() == TokenValueType.NUMBER) {
            return new NumberLiteral(Integer.parseInt(token.getValue()), token.getInitialPosition(), token.getFinalPosition());
        }

        //TODO: Add support for identifiers

        throw new IllegalArgumentException("Unsupported literal type");
    }
}
