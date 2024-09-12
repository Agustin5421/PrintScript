package splitters;

import exceptions.UnsupportedStatementException;
import java.util.ArrayList;
import java.util.List;
import token.Token;

public class MainStatementSplitter {
  private final List<StatementSplitter> statementSplitters =
      List.of(new IfStatementSplitter(), new SemicolonSplitter());

  public List<List<Token>> split(List<Token> tokens) {
    List<List<Token>> statements = new ArrayList<>();
    List<Token> remainingTokens = tokens;

    while (!remainingTokens.isEmpty()) {
      boolean statementFound = false;
      for (StatementSplitter splitter : statementSplitters) {
        if (splitter.shouldSplit(remainingTokens)) {
          SplitResult result = splitter.split(remainingTokens);

          statements.add(result.statement());

          remainingTokens = result.remainingTokens();
          statementFound = true;
        }
      }

      if (!statementFound) {
        throw new UnsupportedStatementException(tokens);
      }
    }
    return statements;
  }
}
