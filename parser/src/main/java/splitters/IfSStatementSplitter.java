package splitters;

import java.util.ArrayList;
import java.util.List;
import lexer.Lexer;
import token.Token;
import token.types.TokenSyntaxType;

public class IfSStatementSplitter implements StatementValidator {

  @Override
  public List<Token> validate(Lexer lexer, List<Token> tokens) {
    List<Token> currentSublist = new ArrayList<>();

    while (lexer.hasNext()) {
      Token token = lexer.next();
      currentSublist.add(token);

      if (token.nodeType().equals(TokenSyntaxType.OPEN_BRACES)) {
        getBodyToken(lexer, tokens, currentSublist);
      }

      if (token.nodeType().equals(TokenSyntaxType.ELSE)) {
        getBodyToken(lexer, tokens, currentSublist);
      }
    }

    return currentSublist;
  }

  private void getBodyToken(Lexer lexer, List<Token> tokens, List<Token> statement) {
    int braceCount = 0;

    while (lexer.hasNext()) {
      Token token = lexer.next();
      if (token.nodeType() == TokenSyntaxType.OPEN_BRACES) {
        braceCount++;
      }
      statement.add(token);

      if (token.nodeType() == TokenSyntaxType.CLOSE_BRACES) {
        braceCount--;
        if (braceCount == 0) {
          break;
        }
      }
    }

    if (braceCount != 0) {
      throw new RuntimeException("Expected '}' to close if-else block but got: EOF");
    }
  }

  @Override
  public boolean shouldSplit(List<Token> tokens) {
    if (tokens.isEmpty()) {
      return false;
    }
    return tokens.get(0).nodeType().equals(TokenSyntaxType.IF);
  }
}
