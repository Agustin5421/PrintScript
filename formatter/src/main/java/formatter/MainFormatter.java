package formatter;

import ast.root.ASTNode;
import ast.root.Program;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import formatter.statement.Formatter;
import java.util.List;

public class MainFormatter {
  private final List<Formatter> formatters;

  public MainFormatter(List<Formatter> formatters) {
    this.formatters = formatters;
  }

  public String format(Program program, String options) {
    JsonObject jsonOptions = JsonParser.parseString(options).getAsJsonObject();
    JsonObject rules = jsonOptions.getAsJsonObject("rules");
    StringBuilder formattedCode = new StringBuilder();
    Formatter formatter;
    for (ASTNode statement : program.statements()) {
      formatter = getValidFormatter(statement);
      formattedCode
          .append(formatter.format(statement, rules, formattedCode.toString()))
          .append("\n");
    }
    return formattedCode.toString();
  }

  private Formatter getValidFormatter(ASTNode statement) {
    for (Formatter formatter : formatters) {
      if (formatter.shouldFormat(statement)) {
        return formatter;
      }
    }
    throw new IllegalArgumentException("No formatter found for this statement");
  }
}
