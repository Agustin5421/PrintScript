package parsers;

import ast.root.ASTNode;
import ast.root.Program;
import parsers.statements.AssignmentParser;
import parsers.statements.CallFunctionParser;
import parsers.statements.StatementParser;
import parsers.statements.VariableDeclarationParser;
import token.Position;
import token.Token;
import token.tokenTypes.TokenTagType;

import java.util.ArrayList;
import java.util.List;


public class Parser {
    private final List<StatementParser> statementParsers;

    public Parser(List<StatementParser> statementParsers) {
        this.statementParsers = statementParsers;
    }

    public Parser() {
        this.statementParsers = List.of(
                new CallFunctionParser(),
                new VariableDeclarationParser(),
                new AssignmentParser()
        );
    }

    public Program parse (List<Token> tokens) {
        List<List<Token>> statements = splitBySemicolon(tokens);

        List<ASTNode> astNodes = new ArrayList<>();
        StatementParser parser;

        for (List<Token> statement : statements) {
            parser = getValidParser(statement);
            ASTNode astNode = parser.parse(statement);
            astNodes.add(astNode);
        }
        Position start = tokens.get(0).getInitialPosition();
        Position end = tokens.get(tokens.size() - 1).getFinalPosition();

        return new Program(astNodes, start, end);
    }

    private StatementParser getValidParser(List<Token> statement) {
        for (StatementParser statementParser : statementParsers) {
            if (statementParser.shouldParse(statement)) {
                return statementParser;
            }
        }
        // TODO find a better way to throw the error
        throw new IllegalArgumentException("Error: No parser found for this statement");
    }


    private static List<List<Token>> splitBySemicolon(List<Token> tokens) {
        List<List<Token>> result = new ArrayList<>();
        List<Token> currentSublist = new ArrayList<>();

        for (Token token : tokens) {
            if (token.getType() == TokenTagType.SEMICOLON) {
                result.add(new ArrayList<>(currentSublist));
                currentSublist.clear();
            } else {
                currentSublist.add(token);
            }
        }

        // Checks if the statement ends with a semicolon
        if (!tokens.isEmpty() && tokens.get(tokens.size() - 1).getType() != TokenTagType.SEMICOLON) {
            throw new IllegalArgumentException("Error: Statement does not end with a semicolon");
        }

        return result;
    }
}
