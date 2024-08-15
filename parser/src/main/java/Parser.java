import ast.ASTNode;
import ast.ParserProvider;
import ast.Program;
import token.Position;
import token.Token;
import token.tokenTypes.TokenTagType;

import java.util.ArrayList;
import java.util.List;


public class Parser {
    public Program parse (List<Token> tokens) {
        List<List<Token>> statements = splitBySemicolon(tokens);

        List<ASTNode> astNodes = new ArrayList<>();

       for (List<Token> statement : statements) {
            ASTNode astNode = ParserProvider.parse(statement);
            astNodes.add(astNode);
        }

        Position start = tokens.get(0).getInitialPosition();
        Position end = tokens.get(tokens.size() - 1).getFinalPosition();

        return new Program(astNodes, start, end);
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
