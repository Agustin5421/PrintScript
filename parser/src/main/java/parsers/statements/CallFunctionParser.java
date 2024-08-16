package parsers.statements;

import ast.identifier.Identifier;
import ast.root.ASTNode;
import ast.statements.CallExpression;
import ast.utils.ExpressionParserProvider;
import token.Token;
import token.Position;
import token.tokenTypes.TokenTagType;
import token.tokenTypes.TokenType;

import java.util.ArrayList;
import java.util.List;

public class CallFunctionParser implements StatementParser {
    private final List<String> reservedWords;

    public CallFunctionParser() {
        reservedWords = List.of("println");
    }

    public CallFunctionParser(List<String> reservedWords) {
        this.reservedWords = reservedWords;
    }

    @Override
    public ASTNode parse(List<Token> tokens) {
        String functionName = tokens.get(0).getValue();
        Position start = tokens.get(0).getInitialPosition();
        Position end = tokens.get(tokens.size() - 1).getFinalPosition();

        //Function name
        Identifier identifier = new Identifier(functionName, start, end);

        //Arguments
        List<Token> subList = tokens.subList(1, tokens.size());
        List<Token> arguments =  extractArguments(subList);

        List<ASTNode> argumentExpressions = new ArrayList<>();
        for (Token token : arguments) {
            ASTNode argument = ExpressionParserProvider.parse(List.of(token));
            argumentExpressions.add(argument);
        }

        boolean optionalParameters = false; //TODO: Implement optional parameters in the future correctly


        return new CallExpression(identifier, argumentExpressions, optionalParameters, start, end);
    }

    @Override
    public boolean shouldParse(List<Token> tokens) {
        return reservedWords.contains(tokens.get(0).getValue());
    }


    static class InvalidSyntaxException extends RuntimeException {
        public InvalidSyntaxException(String message) {
            super(message);
        }
    }

    public static List<Token> extractArguments(List<Token> tokens) {
        List<Token> argumentTokens = new ArrayList<>();
        List<Token> currentArgument = new ArrayList<>();
        boolean inArguments = false;
        int openParentheses = 0;

        for (Token token : tokens) {
            TokenType type = token.getType();

            if (type == TokenTagType.OPEN_PARENTHESIS) {
                if (inArguments) {
                    throw new InvalidSyntaxException("Unexpected '(' while already inside arguments.");
                }
                inArguments = true;
                openParentheses++;
                continue;
            }

            if (type == TokenTagType.CLOSE_PARENTHESIS) {
                if (!inArguments) {
                    throw new InvalidSyntaxException("Unexpected ')' outside of arguments.");
                }
                if (openParentheses == 0) {
                    throw new InvalidSyntaxException("Unmatched ')' encountered.");
                }
                inArguments = false;
                openParentheses--;
                if (!currentArgument.isEmpty()) {
                    argumentTokens.addAll(currentArgument);
                    currentArgument.clear();
                }
                continue;
            }

            if (inArguments) {
                if (type == TokenTagType.COMMA) {
                    if (currentArgument.isEmpty()) {
                        throw new InvalidSyntaxException("Comma without preceding argument.");
                    }
                    argumentTokens.addAll(currentArgument);
                    currentArgument.clear();
                } else {
                    currentArgument.add(token);
                }
            }
        }

        if (openParentheses != 0) {
            throw new InvalidSyntaxException("Unmatched '(' encountered.");
        }

        if (!currentArgument.isEmpty()) {
            argumentTokens.addAll(currentArgument);
        }

        return argumentTokens;
    }


}
