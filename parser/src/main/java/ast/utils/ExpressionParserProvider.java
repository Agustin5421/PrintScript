package ast.utils;

import ast.identifier.IdentifierParser;
import ast.root.ASTNode;
import parsers.expressions.BinaryExpressionParser;
import parsers.expressions.ExpressionParser;
import parsers.expressions.LiteralParser;
import token.Token;

import java.util.List;

public class ExpressionParserProvider {
    private static final List<ExpressionParser> parsers = List.of(
            new BinaryExpressionParser(),
            new LiteralParser(),
            new IdentifierParser()
    );

    public static ASTNode parse(List<Token> statement) {
        for (ExpressionParser parser : parsers) {
            if (parser.shouldParse(statement)) {
                return parser.parse(statement);
            }
        }

        throw new IllegalArgumentException("Error: No parser found for statement");
    }
}
