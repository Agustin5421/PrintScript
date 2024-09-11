package formatter.visitor;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.IfStatement;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;
import formatter.strategy.FormattingStrategy;
import java.util.HashMap;
import java.util.Map;

public class FormatterVisitorV2 implements FormatterVisitor {
  private final String currentCode;
  private final Map<AstNodeType, FormattingStrategy> strategies;
  private final FormatterVisitor visitorV1;
  private final int indentationLevel;
  // The configurable amount of spaces to multiply the indentation level by
  private final int indentSize;

  public FormatterVisitorV2(
      FormatterVisitor visitorV1,
      Map<AstNodeType, FormattingStrategy> strategies,
      String currentCode,
      int indentSize,
      int indentationLevel) {
    this.currentCode = currentCode;
    this.strategies = strategies;
    this.indentationLevel = indentationLevel;
    this.visitorV1 = new FormatterVisitorV1(visitorV1.getStrategies(), currentCode);
    this.indentSize = indentSize;
  }

  @Override
  public String getCurrentCode() {
    return currentCode;
  }

  @Override
  public int getValue() {
    return indentationLevel * indentSize;
  }

  @Override
  public Map<AstNodeType, FormattingStrategy> getStrategies() {
    return new HashMap<>(strategies);
  }

  @Override
  public FormattingStrategy getStrategy(AstNode node) {
    FormattingStrategy strategy = strategies.get(node.getNodeType());
    if (strategy == null) {
      throw new IllegalArgumentException("Strategy not found for nodeType: " + node.getNodeType());
    }
    return strategy;
  }

  @Override
  public FormatterVisitor newVisitor(String newCode) {
    return new FormatterVisitorV2(visitorV1, strategies, newCode, indentSize, indentationLevel);
  }

  // Method used to enter a new level of indentation
  @Override
  public FormatterVisitor cloneVisitor() {
    return enterLevel();
  }

  @Override
  public NodeVisitor visitIfStatement(IfStatement ifStatement) {
    String formattedCode = getStrategy(ifStatement).apply(ifStatement, this);
    formattedCode += "\n";
    return newVisitor(formattedCode);
  }

  @Override
  public NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    return newVisitor(booleanLiteral.value().toString());
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    return newVisitor(
        ((FormatterVisitor) visitorV1.visitCallExpression(callExpression)).getCurrentCode());
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    String formattedCode =
        getStrategy(assignmentExpression).apply(assignmentExpression, this) + "\n";
    return newVisitor(formattedCode);
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    String formattedCode = getStrategy(variableDeclaration).apply(variableDeclaration, this) + "\n";
    return newVisitor(formattedCode);
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    return newVisitorV1((FormatterVisitorV1) visitorV1.visitNumberLiteral(numberLiteral));
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    return newVisitorV1((FormatterVisitorV1) visitorV1.visitStringLiteral(stringLiteral));
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    return newVisitorV1((FormatterVisitorV1) visitorV1.visitIdentifier(identifier));
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    return newVisitorV1((FormatterVisitorV1) visitorV1.visitBinaryExpression(binaryExpression));
  }

  public FormatterVisitorV2 newVisitorV1(FormatterVisitorV1 newVisitorV1) {
    return new FormatterVisitorV2(
        newVisitorV1, strategies, newVisitorV1.getCurrentCode(), indentSize, indentationLevel);
  }

  public FormatterVisitorV2 enterLevel() {
    return new FormatterVisitorV2(
        visitorV1, strategies, currentCode, indentSize, indentationLevel + 1);
  }
}
