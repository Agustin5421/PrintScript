package ast.factory;

import ast.Expression;
import ast.Identifier;
import ast.literal.LiteralFactory;
import token.Token;
import token.tokenTypes.TokenTagType;

public class ArgumentFactory {
    public static Expression createArgument(Token token) {
        if (token.getType() == TokenTagType.IDENTIFIER) {
            return new Identifier(token.getValue(), token.getInitialPosition(), token.getFinalPosition());
        } else {
            try{
                return LiteralFactory.createLiteral(token);
            } catch (IllegalArgumentException e){
                throw new IllegalArgumentException("Unsupported argument type");
            }
        }
    }
}
