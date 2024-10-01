package validators;

import java.util.List;
import lexer.Lexer;
import token.Token;
import token.types.TokenSyntaxType;

public class IfStatementValidator implements StatementValidator {

  @Override
  public List<Token> validate(Lexer lexer, List<Token> tokens) {
    int braceCount = 0;
    boolean hasElse = false;

    while (lexer.hasNext()) {
      Token token = lexer.next();
      tokens.add(token);

      if (token.tokenType() == TokenSyntaxType.OPEN_BRACES) {
        braceCount++;
      }

      if (token.tokenType() == TokenSyntaxType.CLOSE_BRACES) {
        braceCount--;
        if (braceCount == 0) {
          if (lexer.peek() != null && lexer.peek().tokenType() == TokenSyntaxType.ELSE) {
            hasElse = true;
          }
          break;
        }
      }
    }

    if (hasElse) {
      tokens.add(lexer.next());
      while (lexer.hasNext()) {
        Token token = lexer.next();
        tokens.add(token);
        if (token.tokenType().equals(TokenSyntaxType.OPEN_BRACES)) {
          braceCount++;
        }

        if (token.tokenType().equals(TokenSyntaxType.CLOSE_BRACES)) {
          braceCount--;
          if (braceCount == 0) {
            return tokens;
          }
        }
      }
    }

    // TODO: throw exception?
    return tokens;
  }

  @Override
  public boolean shouldSplit(List<Token> tokens) {
    if (tokens.isEmpty()) {
      return false;
    }
    return tokens.get(0).tokenType().equals(TokenSyntaxType.IF);
  }
}
