package formatter;

import formatter.statement.AssignmentFormatter;
import formatter.statement.ExpressionFormatter;
import formatter.statement.Formatter;
import formatter.statement.FunctionCallFormatter;
import formatter.statement.VariableDeclarationFormatter;
import java.util.List;
import observers.Observer;

public class MainFormatterInitializer {
  public static MainFormatter init() {
    ExpressionFormatter expressionFormatter = new ExpressionFormatter();
    List<Formatter> formatters =
        List.of(
            new VariableDeclarationFormatter(expressionFormatter),
            new AssignmentFormatter(expressionFormatter),
            new FunctionCallFormatter(expressionFormatter));
    return new MainFormatter(formatters);
  }

  public static MainFormatter init(Observer observer) {
    ExpressionFormatter expressionFormatter = new ExpressionFormatter();
    List<Formatter> formatters =
        List.of(
            new VariableDeclarationFormatter(expressionFormatter),
            new AssignmentFormatter(expressionFormatter),
            new FunctionCallFormatter(expressionFormatter));
    return new MainFormatter(formatters, List.of(observer));
  }
}
