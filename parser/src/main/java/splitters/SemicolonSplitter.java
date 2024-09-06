package splitters;

import java.util.List;
import token.Token;
import token.types.TokenSyntaxType;

public class SemicolonSplitter implements StatementSplitter {

  @Override
  public List<List<Token>> split(List<Token> tokens) {
    List<List<Token>> statements = new java.util.ArrayList<>();

    List<Token> statement = new java.util.ArrayList<>();

    for (Token token : tokens) {
      statement.add(token);

      if (token.nodeType().equals(TokenSyntaxType.SEMICOLON)) {
        statements.add(statement);
        statement = new java.util.ArrayList<>();
      }
    }

    return statements;
  }

  @Override
  public boolean shouldSplit(List<Token> tokens) {
    if (tokens.isEmpty()) {
      return false;
    }

    for (Token token : tokens) {
      if (token.nodeType().equals(TokenSyntaxType.SEMICOLON)) {
        return true;
      }
    }

    return false;
  }
}
