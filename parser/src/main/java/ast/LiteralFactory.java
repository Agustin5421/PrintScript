package ast;

import token.Token;
import token.tokenTypes.TokenValueType;

public class LiteralFactory {
    public static Literal createLiteral(Token token) {
        if (token.getType() == TokenValueType.STRING) {
            return new StringLiteral(token.getValue());
        }

        if (token.getType() == TokenValueType.NUMBER) {
            return new NumberLiteral(Integer.parseInt(token.getValue()));
        }

        throw new IllegalArgumentException("Unsupported literal type");
    }
}
