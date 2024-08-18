package parsers.expressions;

import ast.root.ASTNode;
import ast.literal.LiteralFactory;
import token.Token;
import token.tokenTypes.TokenValueType;

import java.util.List;

public class LiteralParser implements ExpressionParser {

    @Override
    public ASTNode parse(List<Token> tokens) {
        return LiteralFactory.createLiteral(tokens.get(0));
    }

    @Override
    public boolean shouldParse(List<Token> tokens) {
        return tokens.size() == 1 && tokens.get(0).getType() instanceof TokenValueType;
    }
}