package formatter;

import formatter.statement.AsignmentFormatter;
import formatter.statement.ExpressionFormatter;
import formatter.statement.Formatter;
import formatter.statement.FunctionCallFormatter;
import formatter.statement.VariableDeclarationFormatter;
import java.util.List;

public class MainFormatterInitializer {
  public static MainFormatter init() {
    ExpressionFormatter expressionFormatter = new ExpressionFormatter();
    List<Formatter> formatters =
        List.of(
            new VariableDeclarationFormatter(expressionFormatter),
            new AsignmentFormatter(expressionFormatter),
            new FunctionCallFormatter(expressionFormatter));
    return new MainFormatter(formatters);
  }
}
