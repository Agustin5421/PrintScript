package validators;

import java.util.List;
import lexer.Lexer;
import token.Token;

public interface StatementValidator {
  List<Token> validate(Lexer lexer, List<Token> tokens);

  boolean shouldSplit(List<Token> tokens);
}
