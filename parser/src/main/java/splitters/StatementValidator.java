package splitters;

import java.util.List;
import lexer.Lexer;
import token.Token;

public interface StatementValidator {
  // TODO: make a statement class for list of tokens
  List<Token> validate(Lexer lexer, List<Token> tokens);

  boolean shouldSplit(List<Token> tokens);
}
