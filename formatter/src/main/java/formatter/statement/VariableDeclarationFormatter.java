package formatter.statement;

import ast.expressions.BinaryExpression;
import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.statements.VariableDeclaration;
import com.google.gson.JsonObject;

public class VariableDeclarationFormatter implements Formatter {
  private final ExpressionFormatter expressionFormatter;

  public VariableDeclarationFormatter(ExpressionFormatter expressionFormatter) {
    this.expressionFormatter = expressionFormatter;
  }

  @Override
  public String format(AstNode node, JsonObject rules, String currentProgram) {
    VariableDeclaration varDeclarationNode = (VariableDeclaration) node;
    StringBuilder formattedCode = new StringBuilder();
    JsonObject colonRules = rules.getAsJsonObject("colonRules");
    boolean beforeSpace = colonRules.get("before").getAsBoolean();
    boolean afterSpace = colonRules.get("after").getAsBoolean();
    boolean equalSpace = rules.get("equalSpaces").getAsBoolean();

    formattedCode
        .append("let ")
        .append(cleanIdentifier(varDeclarationNode.identifier()))
        .append(beforeSpace ? " " : "")
        .append(":")
        .append(afterSpace ? " " : "")
        .append(getType(varDeclarationNode.expression()))
        .append(equalSpace ? " = " : "=")
        .append(expressionFormatter.format(varDeclarationNode.expression(), rules, currentProgram))
        .append(";");
    return formattedCode.toString();
  }

  // Removing spaces and line breaks
  private String cleanIdentifier(Identifier identifier) {
    return identifier.name().replaceAll("\\s+", "");
  }

  // Returning the nodeType equivalent of printscript (only working for string and numbers for now)
  private String getType(Expression expression) throws IllegalArgumentException {
    if (containsStringLiteral(expression)) {
      return "string";
    }
    return "number";
  }

  private boolean containsStringLiteral(Expression expression) {
    if (expression instanceof StringLiteral) {
      return true;
    } else if (expression instanceof BinaryExpression binaryExpression) {
      return containsStringLiteral(binaryExpression.left())
          || containsStringLiteral(binaryExpression.right());
    }
    return false;
  }
}
