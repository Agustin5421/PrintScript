package validators;

import exceptions.UnsupportedStatementException;
import java.util.List;
import lexer.Lexer;
import token.Token;
import token.types.TokenSyntaxType;

public class SemicolonStatementValidator implements StatementValidator {
  @Override
  public List<Token> validate(Lexer lexer, List<Token> tokens) {
    while (lexer.hasNext()) {
      Token token = lexer.next();
      tokens.add(token);
      if (token.tokenType().equals(TokenSyntaxType.SEMICOLON)) {
        return tokens;
      }
    }

    throw new UnsupportedStatementException(tokens);
  }

  @Override
  public boolean shouldSplit(List<Token> tokens) {
    return !tokens.isEmpty();
  }
}
