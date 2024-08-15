package parsers;

import ast.*;
import token.Token;
import token.tokenTypes.TokenDataType;
import token.tokenTypes.TokenTagType;


import java.util.List;

public class BinaryExpressionParser implements InstructionParser{
    @Override
    public ASTNode parse(List<Token> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Token list cannot be empty");
        }

        // Find the top-level operator in the expression.
        int operatorIndex = findTopLevelOperator(tokens);

        Token operator = tokens.get(operatorIndex);

        // Divide the tokens into left and right expressions.
        List<Token> leftTokens = tokens.subList(0, operatorIndex);
        List<Token> rightTokens = tokens.subList(operatorIndex + 1, tokens.size());

        // Recursively parse the left and right expressions.
        ASTNode left =  ParserProvider.parse(leftTokens);
        ASTNode right =  ParserProvider.parse(rightTokens);

        return new BinaryExpression(left, right, operator.getValue());
    }

    private int findTopLevelOperator(List<Token> tokens) {
        int level = 0;

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (token.getType() == TokenTagType.OPEN_PARENTHESIS) {
                level++;
            } else if (token.getType() == TokenTagType.CLOSE_PARENTHESIS) {
                level--;
            } else if (level == 0 && isOperator(token)) {
                return i; // Found top-level operator.
            }
        }
        throw new IllegalArgumentException("No valid operator found in tokens");
    }

    private boolean isOperator(Token token) {
        return token.getType() == TokenDataType.OPERAND;
    }

    @Override
    public boolean shouldParse(List<Token> tokens) {
        for (Token token : tokens) {
            if (isOperator(token)) {
                return true;
            }
        }
        return false;
    }
}