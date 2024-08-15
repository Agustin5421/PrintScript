package ast;

import parsers.*;
import token.Token;

import java.util.List;

public class ParserProvider {
    private static final List<InstructionParser> parsers = List.of(
            new CallExpressionParser(),
            new VariableDeclarationParser(),
            new AssignmentExpressionParser(),
            new BinaryExpressionParser(),
            new LiteralParser()
    );

    public static ASTNode parse(List<Token> statement) {
        for (InstructionParser parser : parsers) {
            if (parser.shouldParse(statement)) {
                return parser.parse(statement);
            }
        }

        throw new IllegalArgumentException("Error: No parser found for statement");
    }
}
