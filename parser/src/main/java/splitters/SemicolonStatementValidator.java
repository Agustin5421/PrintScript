package splitters;

import exceptions.UnsupportedStatementException;
import lexer.Lexer;
import token.Token;
import token.types.TokenSyntaxType;

import java.util.List;

public class SemicolonStatementValidator implements StatementValidator {
    @Override
    public List<Token> validate(Lexer lexer, List<Token> tokens) {
        while (lexer.hasNext()) {
            Token token = lexer.next();
            tokens.add(token);
            if (token.type().equals(TokenSyntaxType.SEMICOLON)) {
                return tokens;
            }
        }

        throw new UnsupportedStatementException(tokens.toString());
    }

    @Override
    public boolean shouldSplit(List<Token> tokens) {
        return !tokens.isEmpty();
    }
}
