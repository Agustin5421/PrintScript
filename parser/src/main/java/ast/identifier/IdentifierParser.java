package ast.identifier;

import ast.root.ASTNode;
import parsers.expressions.ExpressionParser;
import token.Token;
import token.tokenTypes.TokenTagType;

import java.util.List;

public class IdentifierParser implements ExpressionParser {
    @Override
    public ASTNode parse(List<Token> tokens) {
        return new Identifier(tokens.get(0).getValue(), tokens.get(0).getInitialPosition(), tokens.get(0).getFinalPosition());
    }

    @Override
    public boolean shouldParse(List<Token> tokens) {
        if (tokens.size() != 1) {
            return false;
        }
        return tokens.get(0).getType() == TokenTagType.IDENTIFIER;
    }
}
