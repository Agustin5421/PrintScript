package exceptions;

import java.util.List;

public class ExceptionMessageBuilder {
  public static String getExceptionMessage(List<String> statement, int row, int col) {
    StringBuilder statementString = new StringBuilder();

    statementString.append("'");
    for (String value : statement) {
      statementString.append(value);
    }

    statementString.append("' ");

    statementString.append("at row: ").append(row);
    statementString.append(", column: ").append(col);
    return statementString.toString();
  }

  public static String getExceptionMessage(String statement, int row, int col) {
    return "'" + statement + "' " + "at row: " + row + ", column: " + col;
  }
}
