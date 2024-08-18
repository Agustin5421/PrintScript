package formatter;

import formatter.statement.*;

import java.util.List;

public class MainFormatterInitializer {
    public static MainFormatter init() {
        ExpressionFormatter expressionFormatter = new ExpressionFormatter();
        List<Formatter> formatters = List.of(
                new VariableDeclarationFormatter(expressionFormatter),
                new AsignmentFormatter(expressionFormatter),
                new FunctionCallFormatter(expressionFormatter)
        );
        return new MainFormatter(formatters);
    }
}
