package formatter.statement;

import ast.identifier.Identifier;
import ast.root.ASTNode;
import ast.statements.AssignmentExpression;
import com.google.gson.JsonObject;

public class AsignmentFormatter implements Formatter {
  private final ExpressionFormatter expressionFormatter;

  public AsignmentFormatter(ExpressionFormatter expressionFormatter) {
    this.expressionFormatter = expressionFormatter;
  }

  @Override
  public boolean shouldFormat(ASTNode statement) {
    return statement instanceof AssignmentExpression;
  }

  @Override
  public String format(ASTNode node, JsonObject rules, String currentProgram) {
    AssignmentExpression assignmentNode = (AssignmentExpression) node;
    StringBuilder formattedCode = new StringBuilder();
    boolean equalSpace = rules.get("equalSpaces").getAsBoolean();
    formattedCode
        .append(cleanIdentifier(assignmentNode.left()))
        .append(equalSpace ? " = " : "=")
        .append(expressionFormatter.format(assignmentNode.right(), rules, currentProgram))
        .append(";");
    return formattedCode.toString();
  }

  // Removing spaces and line breaks
  private String cleanIdentifier(Identifier identifier) {
    return identifier.name().replaceAll("\\s+", "");
  }
}
