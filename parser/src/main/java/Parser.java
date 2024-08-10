import ast.ASTNode;
import ast.Program;
import parsers.InstructionParser;
import token.Token;
import token.tokenTypes.TokenTagType;

import java.util.ArrayList;
import java.util.List;


public class Parser {
    private final List<InstructionParser> parsers;
    public Parser (List<InstructionParser> parsers) {
        this.parsers = parsers;
    }

    public Program parse (List<Token> tokens) {
        List<List<Token>> statements = splitBySemicolon(tokens);

        List<ASTNode> astNodes = new ArrayList<>();


       for (List<Token> statement : statements) {
            for (InstructionParser parser : parsers) {
                if (parser.shouldParse(statement)) {
                    astNodes.add(parser.parse(statement));
                    break;
                }
            }
        }

        return new Program(astNodes);
    }


    private List<List<Token>> splitBySemicolon(List<Token> tokens) {
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
