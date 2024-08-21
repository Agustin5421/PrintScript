package exceptions;

import java.util.List;
import token.Position;
import token.Token;

public class ExceptionMessageBuilder {
  public static String getExceptionMessage(List<Token> statement) {
    StringBuilder statementString = new StringBuilder();

    statementString.append("'");
    for (Token token : statement) {
      statementString.append(token.getValue());
    }

    statementString.append("' ");

    Position position = statement.get(0).getInitialPosition();
    statementString.append("at row: ").append(position.row());
    statementString.append(", column: ").append(position.col());
    return statementString.toString();
  }
}
