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
import java.util.List;
import java.util.Map;
import linter.visitor.report.FullReport;
import linter.visitor.report.Report;
import linter.visitor.strategy.LintingStrategy;

public class LinterVisitorV2 implements LinterVisitor {
  private final FullReport fullReport;
  private final Map<AstNodeType, LintingStrategy> nodesStrategies;
  private final LinterVisitor visitorV1;

  public LinterVisitorV2(
      Map<AstNodeType, LintingStrategy> nodesStrategies, LinterVisitor visitorV1) {
    this(new FullReport(), nodesStrategies, visitorV1);
  }

  public LinterVisitorV2(
      FullReport fullReport,
      Map<AstNodeType, LintingStrategy> nodesStrategies,
      LinterVisitor visitorV1) {
    this.fullReport = fullReport;
    this.nodesStrategies = nodesStrategies;
    this.visitorV1 = visitorV1;
  }

  public FullReport getFullReport() {
    return fullReport;
  }

  public Map<AstNodeType, LintingStrategy> getNodesStrategies() {
    return nodesStrategies;
  }

  public LinterVisitor getVisitorV1() {
    return visitorV1;
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
    ExpressionNode condition = ifStatement.getCondition();
    NodeVisitor visitor = visit(condition);

    for (var statement : ifStatement.getThenBlockStatement()) {
      visitor = visitor.visit(statement);
    }

    for (var statement : ifStatement.getElseBlockStatement()) {
      visitor = visitor.visit(statement);
    }

    FullReport newReport = ((LinterVisitor) visitor).getFullReport();
    LintingStrategy strategy = getNodesStrategies().get(ifStatement.getNodeType());
    if (strategy != null) {
      newReport = strategy.apply(ifStatement, newReport);
    }

    return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
  }

  public NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    LintingStrategy strategy = getNodesStrategies().get(booleanLiteral.getNodeType());

    if (strategy != null) {
      FullReport newReport = strategy.apply(booleanLiteral, getFullReport());
      return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
    }

    return this;
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
      newReport = strategy.apply(callExpression, newReport);
    }

    return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
  }

  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    Identifier left = assignmentExpression.left();
    NodeVisitor visitor = visit(left);

    ExpressionNode right = assignmentExpression.right();
    visitor = visitor.visit(right);

    FullReport newReport = ((LinterVisitor) visitor).getFullReport();
    LintingStrategy strategy = getNodesStrategies().get(assignmentExpression.getNodeType());
    if (strategy != null) {
      newReport = strategy.apply(assignmentExpression, newReport);
    }

    return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
  }

  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    Identifier identifier = variableDeclaration.identifier();
    NodeVisitor visitor = visit(identifier);

    ExpressionNode expression = variableDeclaration.expression();
    visitor = visitor.visit(expression);

    FullReport newReport = ((LinterVisitor) visitor).getFullReport();
    LintingStrategy strategy = getNodesStrategies().get(variableDeclaration.getNodeType());
    if (strategy != null) {
      newReport = strategy.apply(variableDeclaration, newReport);
    }

    return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
  }

  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    LinterVisitor visitor = (LinterVisitor) visitorV1.visit(numberLiteral);
    FullReport newReport = visitor.getFullReport();

    List<Report> reports = newReport.getReports();
    return new LinterVisitorV2(
        getFullReport().addReports(reports), getNodesStrategies(), getVisitorV1());
  }

  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    LinterVisitor visitor = (LinterVisitor) visitorV1.visit(stringLiteral);
    FullReport newReport = visitor.getFullReport();

    List<Report> reports = newReport.getReports();
    return new LinterVisitorV2(
        getFullReport().addReports(reports), getNodesStrategies(), getVisitorV1());
  }

  public NodeVisitor visitIdentifier(Identifier identifier) {
    LinterVisitor visitor = (LinterVisitor) visitorV1.visit(identifier);
    FullReport newReport = visitor.getFullReport();

    List<Report> reports = newReport.getReports();
    return new LinterVisitorV2(
        getFullReport().addReports(reports), getNodesStrategies(), getVisitorV1());
  }

  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    ExpressionNode left = binaryExpression.left();
    NodeVisitor visitor = visit(left);

    ExpressionNode right = binaryExpression.right();
    visitor = visitor.visit(right);

    FullReport newReport = ((LinterVisitor) visitor).getFullReport();
    LintingStrategy strategy = getNodesStrategies().get(binaryExpression.getNodeType());
    if (strategy != null) {
      newReport = strategy.apply(binaryExpression, newReport);
    }

    return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
  }
}
