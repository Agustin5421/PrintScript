package splitters;

import java.util.List;
import token.Token;

public class MainStatementSplitter {
  private final List<StatementSplitter> statementSplitters = List.of(new SemicolonSplitter());

  public List<List<Token>> split(List<Token> tokens) {
    for (StatementSplitter statementSplitter : statementSplitters) {
      if (statementSplitter.shouldSplit(tokens)) {
        return statementSplitter.split(tokens);
      }
    }

    return List.of(tokens);
  }
}
