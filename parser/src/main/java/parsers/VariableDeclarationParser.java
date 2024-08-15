package parsers;

import ast.*;
import token.Token;
import token.Position;

import java.util.List;
import java.util.Objects;

public class VariableDeclarationParser implements InstructionParser {
    @Override
    public ASTNode parse(List<Token> tokens) {
        if (!shouldParse(tokens)) {
            throw new IllegalArgumentException("Invalid tokens for VariableDeclarationParser");
        }

        Position start = tokens.get(0).getInitialPosition();
        Position end = tokens.get(tokens.size() - 1).getFinalPosition();

        Identifier identifier = new Identifier(tokens.get(1).getValue(), start, end);

        if (!tokens.get(2).getValue().equals(":")) {
            throw new IllegalArgumentException("Expected ':' at " + tokens.get(2).getInitialPosition().toString() + " but found " + tokens.get(2).getValue() + " instead.");
        }

        //TODO create an exception for this
        if (!tokens.get(3).getValue().equals("number") && !tokens.get(3).getValue().equals("string")) {
            throw new IllegalArgumentException("Expected 'number' or 'string' at " + tokens.get(3).getInitialPosition().toString() + " but found " + tokens.get(3).getValue() + " instead.");
        }

        ASTNode value = ParserProvider.parse(tokens.subList(5, tokens.size()));

        return new VariableDeclaration(identifier, value, start, end);
    }

    @Override
    public boolean shouldParse(List<Token> tokens) {
        return Objects.equals(tokens.get(0).getValue(), "let");
    }

}
