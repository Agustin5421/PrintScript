package splitters;

import java.util.ArrayList;
import java.util.List;
import token.Token;
import token.types.TokenSyntaxType;

public class IfStatementSplitter implements StatementSplitter {
  @Override
  public SplitResult split(List<Token> tokens) {
    List<Token> statement = new ArrayList<>();
    int bracketCount = 0;
    for (int i = 0; i < tokens.size(); i++) {
      Token token = tokens.get(i);
      statement.add(token);
      if (token.tokenType().equals(TokenSyntaxType.OPEN_BRACES)) {
        bracketCount++;
      }
      if (token.tokenType().equals(TokenSyntaxType.CLOSE_BRACES)) {
        bracketCount--;

        if (bracketCount == 0) {
          if (i + 1 < tokens.size() && tokens.get(i + 1).tokenType().equals(TokenSyntaxType.ELSE)) {
            continue;
          } else {
            return new SplitResult(statement, tokens.subList(i + 1, tokens.size()));
          }
        }
      }
    }

    return new SplitResult(statement, tokens.subList(tokens.size(), tokens.size()));
  }

  @Override
  public boolean shouldSplit(List<Token> tokens) {
    if (tokens.isEmpty()) {
      return false;
    }

    return tokens.get(0).tokenType().equals(TokenSyntaxType.IF);
  }
}
