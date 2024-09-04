package ast.splitters;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

import exceptions.SyntaxException;
import java.util.ArrayList;
import java.util.List;
import token.Token;
import token.types.TokenSyntaxType;

public class StatementSplitter {
  public List<List<Token>> split(List<Token> tokens) {
    List<List<Token>> result = new ArrayList<>();
    List<Token> currentSublist = new ArrayList<>();
    int braceCount = 0;
    boolean inIfStatement = false;

    for (Token token : tokens) {
      if (token.type() != TokenSyntaxType.SEMICOLON) {
        currentSublist.add(token);
      }

      if (token.type() == TokenSyntaxType.CLOSE_BRACES) {
        inIfStatement = true;
      }

      if (inIfStatement) {
        if (token.type() == TokenSyntaxType.OPEN_BRACES) {
          braceCount++;
        } else if (token.type() == TokenSyntaxType.CLOSE_BRACES) {
          braceCount--;
          if (braceCount == 0) {
            inIfStatement = false;
            result.add(new ArrayList<>(currentSublist));
            currentSublist.clear();
          }
        }
      } else if (token.type() == TokenSyntaxType.SEMICOLON) {
        result.add(new ArrayList<>(currentSublist));
        currentSublist.clear();
      }
    }

    // Checks if the statement ends with a semicolon or a complete if-else block
    if (!currentSublist.isEmpty()) {
      Token lastToken = tokens.get(tokens.size() - 1);
      if (lastToken.type() != TokenSyntaxType.SEMICOLON) {
        String message = getExceptionMessage(lastToken.value(), tokens.size(), 1);
        throw new SyntaxException("expected ';' or complete if-else block but got: " + message);
      }

      if (inIfStatement && braceCount != 0) {
        throw new SyntaxException("expected '}' to close if-else block but got: EOF");
      }
      result.add(new ArrayList<>(currentSublist));
    }

    return result;
  }
}