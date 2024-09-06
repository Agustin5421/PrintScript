package splitters;

import java.util.List;
import token.Token;

public interface StatementSplitter {
  SplitResult split(List<Token> tokens);

  boolean shouldSplit(List<Token> tokens);
}
