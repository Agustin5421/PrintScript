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
  private final int indentSize;

  public FormatterVisitorV2(
      FormatterVisitor visitorV1,
      Map<AstNodeType, FormattingStrategy> strategies,
      String currentCode,
      int indentSize) {
    this.currentCode = currentCode;
    this.strategies = strategies;
    this.visitorV1 = new FormatterVisitorV1(visitorV1.getStrategies(), currentCode);
    this.indentSize = indentSize;
  }

  public FormatterVisitorV2(FormatterVisitor visitorV1) {
    this(visitorV1, visitorV1.getStrategies(), visitorV1.getCurrentCode(), 0);
  }

  @Override
  public String getCurrentCode() {
    return currentCode;
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
  public NodeVisitor visitIfStatement(IfStatement ifStatement) {
    return new FormatterVisitorV2(visitorV1, strategies, currentCode, indentSize + 1);
  }

  @Override
  public NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    return new FormatterVisitorV2(
        visitorV1, strategies, booleanLiteral.value().toString(), indentSize);
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    String formattedCode = "\t".repeat(indentSize);
    FormatterVisitor newVisitor = (FormatterVisitor) visitorV1.visitCallExpression(callExpression);
    formattedCode += newVisitor.getCurrentCode();
    return new FormatterVisitorV2(newVisitor, strategies, formattedCode, indentSize);
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    String formattedCode =
        "\t".repeat(indentSize)
            + getStrategy(assignmentExpression).apply(assignmentExpression, this)
            + "\n";
    return new FormatterVisitorV2(visitorV1, strategies, formattedCode, indentSize);
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    String formattedCode =
        "\t".repeat(indentSize)
            + getStrategy(variableDeclaration).apply(variableDeclaration, this)
            + "\n";
    return new FormatterVisitorV2(visitorV1, strategies, formattedCode, indentSize);
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    return new FormatterVisitorV2((FormatterVisitor) visitorV1.visitNumberLiteral(numberLiteral));
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    return new FormatterVisitorV2((FormatterVisitor) visitorV1.visitStringLiteral(stringLiteral));
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    return new FormatterVisitorV2((FormatterVisitor) visitorV1.visitIdentifier(identifier));
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    return new FormatterVisitorV2(
        (FormatterVisitor) visitorV1.visitBinaryExpression(binaryExpression));
  }
}
