package splitters;

import exceptions.UnexpectedTokenException;
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

      if (token.tokenType().equals(TokenSyntaxType.SEMICOLON)) {
        return new SplitResult(statement, tokens.subList(i + 1, tokens.size()));
      }
    }

    throw new UnexpectedTokenException(tokens.get(tokens.size() - 1), ";");
  }

  @Override
  public boolean shouldSplit(List<Token> tokens) {
    if (tokens.isEmpty()) {
      return false;
    }

    for (Token token : tokens) {
      if (token.tokenType().equals(TokenSyntaxType.SEMICOLON)) {
        return true;
      }
    }

    return false;
  }
}
