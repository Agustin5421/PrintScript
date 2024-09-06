package validators;

import java.util.ArrayList;
import java.util.List;
import lexer.Lexer;
import token.Token;

public class MainStatementValidator {
  private final List<StatementValidator> statementValidators = List.of(new IfSStatementValidator());
  private final StatementValidator defaultStatementValidator = new SemicolonStatementValidator();

  public List<Token> getNextStatement(Lexer lexer) {
    List<Token> tokens = new ArrayList<>();

    tokens.add(lexer.next());

    for (StatementValidator statementSplitter : statementValidators) {
      if (statementSplitter.shouldSplit(tokens)) {
        return statementSplitter.validate(lexer, tokens);
      }
    }

    return defaultStatementValidator.validate(lexer, tokens);
  }
}
