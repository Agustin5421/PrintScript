package splitters;

import lexer.Lexer;
import token.Token;

import java.util.List;

public interface StatementValidator {
    //TODO: make a statement class for list of tokens
    List<Token> validate(Lexer lexer, List<Token> tokens);
    boolean shouldSplit(List<Token> tokens);
}
