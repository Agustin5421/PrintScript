package visitors;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.List;
import java.util.regex.Pattern;

public class LinterVisitor implements NodeVisitor {
  private final String report;
  private final String rules;

  public LinterVisitor(String rules) {
    this.report = "";
    this.rules = rules;
  }

  public LinterVisitor(String report, String rules) {
    this.report = report;
    this.rules = rules;
  }

  public String getReport() {
    return report;
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration node) {
    return this;
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    return this;
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    return this;
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression node) {
    List<AstNode> arguments = node.arguments();
    String getArgumentsReport = verifyArguments(arguments);
    String newReport = getNewReport(getArgumentsReport);
    return new LinterVisitor(newReport, rules);
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression node) {
    return this;
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier node) {
    String getIdentifierReport = verifyIdentifier(node);
    String newReport = getNewReport(getIdentifierReport);
    return new LinterVisitor(newReport, rules);
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    return this;
  }

  private String getNewReport(String currentReport) {
    String newReport = getReport();
    if (!currentReport.isEmpty()) {
      newReport += currentReport + "\n";
    }
    return newReport;
  }

  private String verifyIdentifier(Identifier identifier) {
    String getWritingConvention =
        JsonParser.parseString(rules)
            .getAsJsonObject()
            .getAsJsonObject("identifier")
            .get("writingConvention")
            .getAsString();

    if (getWritingConvention.equals("camelCase")) {
      return verifyCamelCase(identifier);
    } else if (getWritingConvention.equals("snakeCase")) {
      return verifySnakeCase(identifier);
    }

    return "";
  }

  private String verifyCamelCase(Identifier identifier) {
    String identifierName = identifier.name();
    if (Pattern.matches("^[a-z]+([A-Z][a-z]+)*$", identifierName)) {
      return "";
    }
    return "Warning from "
        + identifier.start()
        + " to "
        + identifier.end()
        + ": Identifier "
        + identifierName
        + " does not follow camelCase convention";
  }

  private String verifySnakeCase(Identifier identifier) {
    String identifierName = identifier.name();
    if (Pattern.matches("^[a-z]+(_[a-z]+)*$", identifierName)) {
      return "";
    }
    return "Warning from "
        + identifier.start()
        + " to "
        + identifier.end()
        + ": Identifier "
        + identifierName
        + " does not follow snake_case convention";
  }

  public String verifyArguments(List<AstNode> arguments) {
    StringBuilder warnings = new StringBuilder();
    JsonObject callExpressionJsonObject =
        JsonParser.parseString(rules).getAsJsonObject().getAsJsonObject("callExpression");
    boolean acceptsIdentifiers = callExpressionJsonObject.get("acceptIdentifiers").getAsBoolean();
    boolean acceptsLiterals = callExpressionJsonObject.get("acceptLiterals").getAsBoolean();
    boolean acceptsExpressions = callExpressionJsonObject.get("acceptExpressions").getAsBoolean();

    for (AstNode argument : arguments) {
      AstNodeType nodeType = argument.getType();
      if (nodeType == AstNodeType.IDENTIFIER) {
        if (!acceptsIdentifiers) {
          warnings
              .append("Warning from ")
              .append(argument.start())
              .append(" to ")
              .append(argument.end())
              .append(": Identifier is not allowed as CallExpression argument\n");
        }
      } else if (nodeType == AstNodeType.STRING_LITERAL || nodeType == AstNodeType.NUMBER_LITERAL) {
        if (!acceptsLiterals) {
          warnings
              .append("Warning from ")
              .append(argument.start())
              .append(" to ")
              .append(argument.end())
              .append(": Literal is not allowed as CallExpression argument\n");
        }
      } else {
        if (!acceptsExpressions) {
          warnings
              .append("Warning from ")
              .append(argument.start())
              .append(" to ")
              .append(argument.end())
              .append(": Expression is not allowed as CallExpression argument\n");
        }
      }
    }

    return warnings.toString();
  }
}
