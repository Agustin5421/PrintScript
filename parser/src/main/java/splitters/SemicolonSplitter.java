package splitters;

import exceptions.UnsupportedStatementException;
import java.util.ArrayList;
import java.util.List;
import token.Token;
import token.types.TokenSyntaxType;

public class SemicolonSplitter implements StatementSplitter {

  @Override
  public SplitResult split(List<Token> tokens) {
    List<Token> statement = new ArrayList<>();

    for (int i = 0; i < tokens.size(); i++) {
      Token token = tokens.get(i);
      statement.add(token);

      if (token.nodeType().equals(TokenSyntaxType.SEMICOLON)) {
        return new SplitResult(statement, tokens.subList(i + 1, tokens.size()));
      }
    }

    throw new UnsupportedStatementException("No semicolon found in statement" + statement);
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
