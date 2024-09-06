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

      if (token.type() == TokenSyntaxType.OPEN_BRACES) {
        braceCount++;
      }

      if (token.type() == TokenSyntaxType.CLOSE_BRACES) {
        braceCount--;
        if (braceCount == 0) {
          try {
            if (lexer.peek().type() == TokenSyntaxType.ELSE) {
              hasElse = true;
            }
          } catch (IndexOutOfBoundsException ignored) {
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
        if (token.type().equals(TokenSyntaxType.OPEN_BRACES)) {
          braceCount++;
        }

        if (token.type().equals(TokenSyntaxType.CLOSE_BRACES)) {
          braceCount--;
          if (braceCount == 0) {
            return tokens;
          }
        }
      }
    }

    throw new RuntimeException("Expected '}' to close if-else block but got: EOF");
  }

  @Override
  public boolean shouldSplit(List<Token> tokens) {
    if (tokens.isEmpty()) {
      return false;
    }
    return tokens.get(0).type().equals(TokenSyntaxType.IF);
  }
}
