package splitters;

import java.util.List;
import token.Token;

public interface StatementSplitter {
  List<List<Token>> split(List<Token> tokens);

  boolean shouldSplit(List<Token> tokens);
}
