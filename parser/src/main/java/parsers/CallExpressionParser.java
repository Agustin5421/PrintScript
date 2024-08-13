package parsers;

import ast.*;
import token.Token;
import token.tokenTypes.TokenTagType;
import token.tokenTypes.TokenType;

import java.util.ArrayList;
import java.util.List;

public class CallExpressionParser implements InstructionParser{
    private final List<String> reservedWords = List.of("println");

    @Override
    public ASTNode parse(List<Token> tokens) {
        String functionName = tokens.get(0).getValue();
        int start = tokens.get(0).getCol();
        int end = tokens.get(tokens.size() - 1).getCol();

        //Function name
        Identifier identifier = new Identifier(functionName); //TODO: add start and end

        //Arguments
        List<Token> subList = tokens.subList(1, tokens.size());
        List<Token> arguments =  extractArguments(subList);

        List<Literal> argumentLiterals = new ArrayList<>();
        for (Token token : arguments) {
            argumentLiterals.add(LiteralFactory.createLiteral(token));
        }

        return new CallExpression(identifier, argumentLiterals);
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
                // Añadir el argumento acumulado si existe al final
                if (!currentArgument.isEmpty()) {
                    argumentTokens.addAll(currentArgument);
                    currentArgument.clear();
                }
                continue;
            }

            if (inArguments) {
                if (type == TokenTagType.COMMA) {
                    // Añadir el argumento acumulado si existe
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

        // Verificar paréntesis desbalanceados
        if (openParentheses != 0) {
            throw new InvalidSyntaxException("Unmatched '(' encountered.");
        }

        // Añadir el último argumento si existe
        if (!currentArgument.isEmpty()) {
            argumentTokens.addAll(currentArgument);
        }

        return argumentTokens;
    }


}
