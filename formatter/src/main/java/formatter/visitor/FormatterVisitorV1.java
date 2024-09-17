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
import formatter.strategy.common.BinaryExpressionStrategy;
import java.util.HashMap;
import java.util.Map;

public class FormatterVisitorV1 implements FormatterVisitor {
  private final String currentCode;
  private final Map<AstNodeType, FormattingStrategy> strategies;

  public FormatterVisitorV1(Map<AstNodeType, FormattingStrategy> strategies, String formattedCode) {
    this.currentCode = formattedCode;
    this.strategies = strategies;
  }

  public FormatterVisitorV1(Map<AstNodeType, FormattingStrategy> strategies) {
    this(strategies, "");
  }

  @Override
  public NodeVisitor visit(AstNode node) {
    if (node instanceof VariableDeclaration) {
      return visitVarDec((VariableDeclaration) node);
    } else if (node instanceof IfStatement) {
      return visitIfStatement((IfStatement) node);
    } else if (node instanceof BooleanLiteral) {
      return visitBooleanLiteral((BooleanLiteral) node);
    } else if (node instanceof CallExpression) {
      return visitCallExpression((CallExpression) node);
    } else if (node instanceof AssignmentExpression) {
      return visitAssignmentExpression((AssignmentExpression) node);
    } else if (node instanceof BinaryExpression) {
      return visitBinaryExpression((BinaryExpression) node);
    } else if (node instanceof NumberLiteral) {
      return visitNumberLiteral((NumberLiteral) node);
    } else if (node instanceof StringLiteral) {
      return visitStringLiteral((StringLiteral) node);
    } else if (node instanceof Identifier) {
      return visitIdentifier((Identifier) node);
    }
    return this;
  }

  public NodeVisitor visitIfStatement(IfStatement ifStatement) {
    throw new IllegalArgumentException("If Node not supported in this version :( ");
  }

  public NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    throw new IllegalArgumentException("Boolean Node not supported in this version :( ");
  }

  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    FormattingStrategy strategy = getStrategy(callExpression);
    String formattedCode = strategy.apply(callExpression, this);
    formattedCode += "\n";
    return newVisitor(formattedCode);
  }

  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    FormattingStrategy strategy = getStrategy(assignmentExpression);
    String formattedCode = strategy.apply(assignmentExpression, this);
    formattedCode += "\n";
    return newVisitor(formattedCode);
  }

  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    FormattingStrategy strategy = getStrategy(variableDeclaration);
    String formattedCode = strategy.apply(variableDeclaration, this);
    formattedCode += "\n";
    return newVisitor(formattedCode);
  }

  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    return newVisitor(numberLiteral.value().toString());
  }

  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    return newVisitor("\"" + stringLiteral.value() + "\"");
  }

  public NodeVisitor visitIdentifier(Identifier identifier) {
    return newVisitor(identifier.name());
  }

  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    BinaryExpressionStrategy strategy = new BinaryExpressionStrategy(binaryExpression.operator());
    return newVisitor(strategy.apply(binaryExpression, this));
  }

  @Override
  public String getCurrentCode() {
    return currentCode;
  }

  @Override
  public int getValue() {
    return 0;
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
    return new FormatterVisitorV1(strategies, newCode);
  }

  @Override
  public FormatterVisitor cloneVisitor() {
    return new FormatterVisitorV1(strategies, currentCode);
  }
}
