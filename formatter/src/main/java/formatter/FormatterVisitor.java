package formatter;

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
import formatter.strategy.common.BinaryExpressionStrategy;
import java.util.Map;

public class FormatterVisitor implements NodeVisitor {
  private final String currentCode;
  private final Map<AstNodeType, FormattingStrategy> strategies;

  public FormatterVisitor(Map<AstNodeType, FormattingStrategy> strategies, String formattedCode) {
    this.currentCode = formattedCode;
    this.strategies = strategies;
  }

  public FormatterVisitor(Map<AstNodeType, FormattingStrategy> strategies) {
    this(strategies, "");
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
    FormattingStrategy strategy = getStrategy(callExpression);
    String formattedCode = strategy.apply(callExpression, this);
    formattedCode += "\n";
    return new FormatterVisitor(strategies, formattedCode);
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    FormattingStrategy strategy = getStrategy(assignmentExpression);
    String formattedCode = strategy.apply(assignmentExpression, this);
    formattedCode += "\n";
    return new FormatterVisitor(strategies, formattedCode);
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    FormattingStrategy strategy = getStrategy(variableDeclaration);
    String formattedCode = strategy.apply(variableDeclaration, this);
    formattedCode += "\n";
    return new FormatterVisitor(strategies, formattedCode);
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    return new FormatterVisitor(strategies, numberLiteral.value().toString());
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    return new FormatterVisitor(strategies, stringLiteral.value());
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    return new FormatterVisitor(strategies, identifier.name());
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    BinaryExpressionStrategy strategy = new BinaryExpressionStrategy(binaryExpression.operator());
    return new FormatterVisitor(strategies, strategy.apply(binaryExpression, this));
  }

  public String getCurrentCode() {
    return currentCode;
  }

  public FormattingStrategy getStrategy(AstNode node) throws IllegalArgumentException {
    FormattingStrategy strategy = strategies.get(node.getNodeType());
    if (strategy == null) {
      throw new IllegalArgumentException("Strategy not found for nodeType: " + node.getNodeType());
    }
    return strategy;
  }
}
