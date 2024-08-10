package parsers;

import ast.ASTNode;
import token.Token;

import java.util.List;

public class StatementParser implements InstructionParser {
    private final List<InstructionParser> parsers;

    public StatementParser(List<InstructionParser> parsers) {
        this.parsers = parsers;
    }

    @Override
    public ASTNode parse(List<Token> tokens) {
        for (InstructionParser parser : parsers) {
            if (parser.shouldParse(tokens)) {
                return parser.parse(tokens);
            }
        }

        //TODO throw exception here
        throw new IllegalArgumentException("No parser found for tokens: " + tokens);
    }

    @Override
    public boolean shouldParse(List<Token> tokens) {
        for (InstructionParser parser : parsers) {
            if (parser.shouldParse(tokens)) {
                return true;
            }
        }
        return false;
    }
}
//TODO this is weird