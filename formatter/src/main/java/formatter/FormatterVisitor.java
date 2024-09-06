package formatter;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.IfStatement;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;
import com.google.gson.JsonObject;
import formatter.statement.AssignmentFormatter;
import formatter.statement.ExpressionFormatter;
import formatter.statement.FunctionCallFormatter;
import formatter.statement.VariableDeclarationFormatter;

public class FormatterVisitor implements NodeVisitor {
  private final String formattedCode;
  private final JsonObject options;

  public FormatterVisitor(JsonObject options, String formattedCode) {
    this.formattedCode = formattedCode;
    this.options = options;
  }

  @Override
  public NodeVisitor visitIfStatement(IfStatement ifStatement) {
    throw new IllegalArgumentException("If Node not supported in this version :( ");
  }

  @Override
  public NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    throw new IllegalArgumentException("Boolean Node not supported in this version :( ");
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    FunctionCallFormatter formatter = new FunctionCallFormatter(new ExpressionFormatter());
    return new FormatterVisitor(options, formatter.format(callExpression, options, formattedCode));
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    AssignmentFormatter formatter = new AssignmentFormatter(new ExpressionFormatter());
    return new FormatterVisitor(
        options, formatter.format(assignmentExpression, options, formattedCode));
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration node) {
    VariableDeclarationFormatter formatter =
        new VariableDeclarationFormatter(new ExpressionFormatter());
    return new FormatterVisitor(options, formatter.format(node, options, formattedCode));
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    ExpressionFormatter formatter = new ExpressionFormatter();
    return new FormatterVisitor(options, formatter.format(numberLiteral, options, formattedCode));
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    ExpressionFormatter formatter = new ExpressionFormatter();
    return new FormatterVisitor(options, formatter.format(stringLiteral, options, formattedCode));
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    ExpressionFormatter formatter = new ExpressionFormatter();
    return new FormatterVisitor(options, formatter.format(identifier, options, formattedCode));
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    ExpressionFormatter formatter = new ExpressionFormatter();
    return new FormatterVisitor(
        options, formatter.format(binaryExpression, options, formattedCode));
  }

  public String getCurrentCode() {
    return formattedCode;
  }
}
