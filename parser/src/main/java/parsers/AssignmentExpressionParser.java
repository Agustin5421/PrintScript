package parsers;

import ast.ASTNode;
import ast.AssignmentExpression;
import ast.Identifier;
import token.Token;
import token.tokenTypes.TokenTagType;

import java.util.List;

public class AssignmentExpressionParser implements InstructionParser{

    @Override
    public ASTNode parse(List<Token> tokens) {
        validateSyntax(tokens);

        Identifier left = new Identifier(tokens.get(0).getValue());
        Identifier right = new Identifier(tokens.get(2).getValue());
        return new AssignmentExpression(left, right, tokens.get(1).getValue());
    }

    private void validateSyntax(List<Token> tokens) {
        if (!shouldParse(tokens)) {
            throw new IllegalArgumentException("Invalid tokens for AssignmentExpression");
        }

        if (!tokens.get(1).getValue().equals("=")) {
            throw new IllegalArgumentException("Invalid tokens for AssignmentExpression");
        }

        if (isCorrect(tokens)) {
            throw new IllegalArgumentException("Invalid tokens for AssignmentExpression");
        }
    }

    private static boolean isCorrect(List<Token> tokens) {
        return tokens.get(0).getType() != TokenTagType.IDENTIFIER && tokens.get(2).getType() != TokenTagType.IDENTIFIER;
    }

    @Override
    public boolean shouldParse(List<Token> tokens) {
        if (tokens.size() != 3 ) {
            return false;
        }
        return tokens.get(0).getType() == TokenTagType.IDENTIFIER;
    }
}
