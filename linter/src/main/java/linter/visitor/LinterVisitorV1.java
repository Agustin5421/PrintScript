package linter.visitor;

import ast.expressions.BinaryExpression;
import ast.expressions.ExpressionNode;
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
import java.util.Map;
import linter.visitor.report.FullReport;
import linter.visitor.strategy.LintingStrategy;

public class LinterVisitorV1 implements LinterVisitor {
  private final FullReport fullReport;
  private final Map<AstNodeType, LintingStrategy> nodesStrategies;

  public LinterVisitorV1(Map<AstNodeType, LintingStrategy> nodesStrategies) {
    this(new FullReport(), nodesStrategies);
  }

  public LinterVisitorV1(FullReport fullReport, Map<AstNodeType, LintingStrategy> nodesStrategies) {
    this.fullReport = fullReport;
    this.nodesStrategies = nodesStrategies;
  }

  public FullReport getFullReport() {
    return fullReport;
  }

  public Map<AstNodeType, LintingStrategy> getNodesStrategies() {
    return nodesStrategies;
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

  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    Identifier identifier = variableDeclaration.identifier();
    NodeVisitor visitor = visit(identifier);

    ExpressionNode expression = variableDeclaration.expression();
    visitor = visitor.visit(expression);

    FullReport newReport = ((LinterVisitor) visitor).getFullReport();
    LintingStrategy strategy = getNodesStrategies().get(variableDeclaration.getNodeType());
    if (strategy != null) {
      newReport = strategy.oldApply(variableDeclaration, newReport);
    }

    return new LinterVisitorV1(newReport, getNodesStrategies());
  }

  public NodeVisitor visitIfStatement(IfStatement ifStatement) {
    throw new IllegalArgumentException("If Node not supported in this version :( ");
  }

  public NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    throw new IllegalArgumentException("Boolean Node not supported in this version :( ");
  }

  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    Identifier methodIdentifier = callExpression.methodIdentifier();
    NodeVisitor visitor = visit(methodIdentifier);

    for (AstNode argument : callExpression.arguments()) {
      visitor = visitor.visit(argument);
    }

    FullReport newReport = ((LinterVisitor) visitor).getFullReport();
    LintingStrategy strategy = getNodesStrategies().get(callExpression.getNodeType());
    if (strategy != null) {
      newReport = strategy.oldApply(callExpression, newReport);
    }

    return new LinterVisitorV1(newReport, getNodesStrategies());
  }

  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    Identifier left = assignmentExpression.left();
    NodeVisitor visitor = visit(left);

    ExpressionNode right = assignmentExpression.right();
    visitor = visitor.visit(right);

    FullReport newReport = ((LinterVisitor) visitor).getFullReport();
    LintingStrategy strategy = getNodesStrategies().get(assignmentExpression.getNodeType());
    if (strategy != null) {
      newReport = strategy.oldApply(assignmentExpression, newReport);
    }

    return new LinterVisitorV1(newReport, getNodesStrategies());
  }

  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    ExpressionNode left = binaryExpression.left();
    NodeVisitor visitor = visit(left);

    ExpressionNode right = binaryExpression.right();
    visitor = visitor.visit(right);

    FullReport newReport = ((LinterVisitor) visitor).getFullReport();
    LintingStrategy strategy = getNodesStrategies().get(binaryExpression.getNodeType());
    if (strategy != null) {
      newReport = strategy.oldApply(binaryExpression, newReport);
    }

    return new LinterVisitorV1(newReport, getNodesStrategies());
  }

  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    LintingStrategy strategy = getNodesStrategies().get(numberLiteral.getNodeType());

    if (strategy != null) {
      FullReport newReport = strategy.oldApply(numberLiteral, getFullReport());
      return new LinterVisitorV1(newReport, getNodesStrategies());
    }

    return this;
  }

  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    LintingStrategy strategy = getNodesStrategies().get(stringLiteral.getNodeType());

    if (strategy != null) {
      FullReport newReport = strategy.oldApply(stringLiteral, getFullReport());
      return new LinterVisitorV1(newReport, getNodesStrategies());
    }

    return this;
  }

  public NodeVisitor visitIdentifier(Identifier identifier) {
    LintingStrategy strategy = getNodesStrategies().get(identifier.getNodeType());

    if (strategy != null) {
      FullReport newReport = strategy.oldApply(identifier, getFullReport());
      return new LinterVisitorV1(newReport, getNodesStrategies());
    }

    return this;
  }
}
