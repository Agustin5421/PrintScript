package splitters;

import java.util.ArrayList;
import java.util.List;
import lexer.Lexer;
import token.Token;

public class MainStatementValidator {
  private final List<StatementValidator> statementSplitters = List.of(new IfSStatementSplitter());

  private final StatementValidator defaultStatementValidator = new SemicolonStatementValidator();

  public List<Token> getNextStatement(Lexer lexer) {
    List<Token> tokens = new ArrayList<>();

    tokens.add(lexer.next());

    for (StatementValidator statementSplitter : statementSplitters) {
      if (statementSplitter.shouldSplit(tokens)) {
        return statementSplitter.validate(lexer, tokens);
      }
    }

    return defaultStatementValidator.validate(lexer, tokens);
  }
}
